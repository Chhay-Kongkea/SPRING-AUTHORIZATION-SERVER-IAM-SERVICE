package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.ResetUserPasswordCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Objects;

public class ResetUserPasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final CurrentActorProvider currentActorProvider;

    public ResetUserPasswordUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public void execute(ResetUserPasswordCommand command) {

        Objects.requireNonNull(command, "Reset password command cannot be null");
        Objects.requireNonNull(command.userId(), "User ID cannot be null");

        validate(command);

        User user = userRepository.findById(new UserId(command.userId()))
                .orElseThrow(() ->
                        new NotFoundDomainException(
                                "User not found: " + command.userId()
                        )
                );

        String newPasswordHash = passwordHasher.hash(command.newPassword());

        user.changePassword(newPasswordHash, currentActorProvider.getCurrentActor());

        userRepository.save(user);
    }

    private void validate(ResetUserPasswordCommand command) {

        if (command.newPassword() == null ||
                command.newPassword().isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }

        if (command.newPassword().length() < 12) {
            throw new IllegalArgumentException(
                    "New password must contain at least 12 characters"
            );
        }
    }
}