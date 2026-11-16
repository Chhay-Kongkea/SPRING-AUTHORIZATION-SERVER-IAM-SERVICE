package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.CreateUserCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Locale;
import java.util.Objects;

public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final CurrentActorProvider currentActorProvider;

    public CreateUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public UserResult execute(CreateUserCommand command) {

        Objects.requireNonNull(command, "Create user command cannot be null");

        String username = requireText(command.username(), "Username");
        String email = requireText(command.email(), "Email")
                .toLowerCase(Locale.ROOT);
        String rawPassword = requireText(command.rawPassword(), "Password");
        String givenName = requireText(command.givenName(), "Given name");
        String familyName = requireText(command.familyName(), "Family name");

        if (rawPassword.length() < 12) {
            throw new IllegalArgumentException(
                    "Password must contain at least 12 characters"
            );
        }

        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException(
                    "Username already exists: " + username
            );
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException(
                    "Email already exists: " + email
            );
        }

        String passwordHash = passwordHasher.hash(rawPassword);
        String actor = currentActorProvider.getCurrentActor();

        User user = new User(
                UserId.generate(),
                username,
                email,
                passwordHash,
                givenName,
                familyName,
                command.locale() == null || command.locale().isBlank() ? "en" : command.locale(),
                actor
        );

        user.updateProfile(
                givenName, familyName, command.middleName(), command.nickname(), command.gender(),
                command.birthdate(), command.profileUrl(), command.pictureUrl(), command.websiteUrl(),
                command.zoneInfo(), command.locale(), command.phoneNumber(), command.streetAddress(),
                command.locality(), command.region(), command.postalCode(), command.country(), actor);

        if (command.emailVerified()) {
            user.verifyEmail(actor);
        }

        User savedUser = userRepository.save(user);

        return new UserResult(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.isEmailVerified(),
                savedUser.getGivenName(),
                savedUser.getFamilyName(),
                savedUser.getLocale(),
                savedUser.isEnabled(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank"
            );
        }

        return value.trim();
    }
}