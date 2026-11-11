package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.RecordLoginAttemptCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;

import java.util.Locale;
import java.util.Objects;

public class RecordLoginAttemptUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;

    public RecordLoginAttemptUseCase(UserRepository userRepository, CurrentActorProvider currentActorProvider) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public void execute(RecordLoginAttemptCommand command) {

        Objects.requireNonNull(command, "Command cannot be null");

        if (command.usernameOrEmail() == null
                || command.usernameOrEmail().isBlank()) {
            throw new IllegalArgumentException(
                    "Username or email cannot be blank"
            );
        }

        String identifier = command.usernameOrEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() -> new NotFoundDomainException(
                        "User not found"
                ));

        if (command.successful()) {
            user.recordSuccessfulLogin(currentActorProvider.getCurrentActor());
        } else {
            user.recordFailedLogin(currentActorProvider.getCurrentActor());
        }

        userRepository.save(user);
    }
}