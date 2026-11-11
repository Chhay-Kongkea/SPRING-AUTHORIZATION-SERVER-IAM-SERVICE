package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.ChangePasswordCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.InvalidCredentialsDomainException;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Objects;

public class ChangePasswordUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final CurrentActorProvider currentActorProvider;

    public ChangePasswordUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public void execute(ChangePasswordCommand command) {

        Objects.requireNonNull(command, "Change password command cannot be null");
        Objects.requireNonNull(command.userId(), "User ID cannot be null");

        validate(command);

        User user = userRepository.findById(new UserId(command.userId()))
                .orElseThrow(() ->
                        new NotFoundDomainException(
                                "User not found: " + command.userId()
                        )
                );

        String currentPasswordHash = user.getPasswordHash();

        if (currentPasswordHash == null ||
                !passwordHasher.matches(
                        command.currentPassword(),
                        currentPasswordHash
                )) {
            throw new InvalidCredentialsDomainException(
                    "Current password is incorrect"
            );
        }

        if (passwordHasher.matches(
                command.newPassword(),
                currentPasswordHash
        )) {
            throw new IllegalArgumentException(
                    "New password must be different from the current password"
            );
        }

        String newPasswordHash = passwordHasher.hash(command.newPassword());

        user.changePassword(newPasswordHash, currentActorProvider.getCurrentActor());

        userRepository.save(user);
    }

    private void validate(ChangePasswordCommand command) {

        if (command.currentPassword() == null ||
                command.currentPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "Current password is required"
            );
        }

        if (command.newPassword() == null ||
                command.newPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "New password is required"
            );
        }

        if (command.newPassword().length() < 12) {
            throw new IllegalArgumentException(
                    "New password must contain at least 12 characters"
            );
        }
    }
}