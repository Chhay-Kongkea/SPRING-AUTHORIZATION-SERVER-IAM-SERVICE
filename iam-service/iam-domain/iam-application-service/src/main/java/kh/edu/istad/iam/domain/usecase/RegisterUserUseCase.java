package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.RegisterUserCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.ConflictDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.valueobject.RoleId;
import java.time.Instant;
import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Locale;
import java.util.Objects;



public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final CurrentActorProvider currentActorProvider;
    private final RoleRepository roleRepository;

    public RegisterUserUseCase(
            UserRepository userRepository,
            PasswordHasher passwordHasher,
            CurrentActorProvider currentActorProvider,
            RoleRepository roleRepository
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
        this.roleRepository = Objects.requireNonNull(roleRepository);
    }

    public UserResult execute(RegisterUserCommand command) {

        Objects.requireNonNull(command, "Registration command cannot be null");

        validate(command);

        String username = command.username().trim();
        String email = command.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            throw new ConflictDomainException("Username is already taken");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ConflictDomainException("Email is already registered");
        }

        String passwordHash = passwordHasher.hash(command.rawPassword());

        String actor = currentActorProvider.getCurrentActor();

        User user = new User(
                UserId.generate(),
                username,
                email,
                passwordHash,
                command.givenName(),
                command.familyName(),
                command.locale(),
                actor
        );

        var defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default USER role has not been seeded"));
        if (!defaultRole.isActive() || defaultRole.isDeleted()) {
            throw new IllegalStateException("Default USER role is not assignable");
        }
        user.assignRole(defaultRole.getId(), actor, null, "Default registration role");

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

    private void validate(RegisterUserCommand command) {

        if (command.username() == null || command.username().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (command.email() == null || command.email().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (command.rawPassword() == null || command.rawPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        if (command.rawPassword().length() < 12) {
            throw new IllegalArgumentException(
                    "Password must contain at least 12 characters"
            );
        }

        if (command.givenName() == null || command.givenName().isBlank()) {
            throw new IllegalArgumentException("Given name is required");
        }

        if (command.familyName() == null || command.familyName().isBlank()) {
            throw new IllegalArgumentException("Family name is required");
        }

        if (command.locale() == null || command.locale().isBlank()) {
            throw new IllegalArgumentException("Locale is required");
        }
    }
}