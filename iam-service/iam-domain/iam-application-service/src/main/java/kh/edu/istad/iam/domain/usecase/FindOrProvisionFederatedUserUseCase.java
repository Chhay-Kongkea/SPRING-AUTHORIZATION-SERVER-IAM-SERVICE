
package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.FindOrProvisionFederatedUserCommand;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.result.FederatedUserResult;
import kh.edu.istad.iam.domain.valueobject.IdentityId;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Locale;
import java.util.UUID;

public class FindOrProvisionFederatedUserUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;
    private final CurrentActorProvider currentActorProvider;
    private final RoleRepository roleRepository;

    public FindOrProvisionFederatedUserUseCase(
            UserRepository userRepository,
            IdentityRepository identityRepository,
            CurrentActorProvider currentActorProvider,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.identityRepository = identityRepository;
        this.currentActorProvider = currentActorProvider;
        this.roleRepository = roleRepository;
    }

    public FederatedUserResult execute(
            FindOrProvisionFederatedUserCommand command
    ) {

        if (command == null) {
            throw new IllegalArgumentException("Command is required");
        }

        if (command.provider() == null) {
            throw new IllegalArgumentException("Identity provider is required");
        }

        if (command.providerSubject() == null
                || command.providerSubject().isBlank()) {
            throw new IllegalArgumentException("Provider subject is required");
        }

        String actor = currentActorProvider.getCurrentActor();

        var existingIdentity = identityRepository.findByProviderAndSubject(
                command.provider(),
                command.providerSubject().trim()
        );

        if (existingIdentity.isPresent()) {

            Identity identity = existingIdentity.get();

            if (identity.isDeleted()) {
                throw new IllegalStateException(
                        "This external identity has been unlinked"
                );
            }

            User user = userRepository.findById(identity.getUserId())
                    .orElseThrow(() ->
                            new IllegalStateException(
                                    "Linked IAM user does not exist"
                            )
                    );

            if (user.isDeleted() || !user.isEnabled()) {
                throw new IllegalStateException(
                        "User account is not available"
                );
            }

            identity.updateProviderProfile(
                    command.email(),
                    command.emailVerified(),
                    command.displayName(),
                    command.username(),
                    command.pictureUrl(),
                    command.attributes(),
                    actor
            );

            identity.recordLogin(actor);
            identityRepository.save(identity);

            user.recordSuccessfulLogin(actor);
            userRepository.save(user);

            return new FederatedUserResult(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    false
            );
        }

        String email = normalizeAndValidateEmail(command.email());

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException(
                    "An account with this email already exists. "
                            + "Authenticate with that account and link "
                            + "the external identity explicitly."
            );
        }

        String username = resolveUsername(command.username(), email);

        String displayName = command.displayName();

        if (displayName == null || displayName.isBlank()) {
            displayName = username;
        }

        String fullName = command.displayName() == null || command.displayName().isBlank()
                ? username : command.displayName().trim();
        String[] nameParts = fullName.split("\\s+", 2);
        String givenName = nameParts[0];
        String familyName = nameParts.length > 1 ? nameParts[1] : "User";

        User user = new User(
                UserId.generate(),
                username,
                email,
                null,
                givenName,
                familyName,
                "en",
                actor
        );

        var defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default USER role has not been seeded"));
        if (!defaultRole.isActive() || defaultRole.isDeleted()) {
            throw new IllegalStateException("Default USER role is not assignable");
        }
        user.assignRole(defaultRole.getId(), actor, null, "Default federated registration role");

        if (command.emailVerified()) {
            user.verifyEmail(actor);
        }

        User savedUser = userRepository.save(user);

        Identity identity = new Identity(
                IdentityId.generate(),
                savedUser.getId(),
                command.provider(),
                command.providerSubject().trim(),
                email,
                command.emailVerified(),
                command.displayName(),
                command.username(),
                command.pictureUrl(),
                command.attributes(),
                actor
        );

        identity.recordLogin(actor);

        identityRepository.save(identity);

        return new FederatedUserResult(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                true
        );
    }

    private String normalizeAndValidateEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required to provision a federated user"
            );
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

        String emailPattern =
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$";

        if (!normalizedEmail.matches(emailPattern)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        return normalizedEmail;
    }

    private String resolveUsername(String providerUsername, String email) {

        String candidate = providerUsername;

        if (candidate == null || candidate.isBlank()) {
            candidate = email.substring(0, email.indexOf('@'));
        }

        candidate = candidate.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9._-]", "");

        if (candidate.isBlank()) {
            candidate = "user";
        }

        if (candidate.length() > 40) {
            candidate = candidate.substring(0, 40);
        }

        if (!userRepository.existsByUsername(candidate)) {
            return candidate;
        }

        return candidate + "_"
                + UUID.randomUUID().toString().substring(0, 8);
    }
}
