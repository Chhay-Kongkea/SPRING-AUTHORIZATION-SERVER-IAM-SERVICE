package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.LinkIdentityCommand;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.IdentityResult;
import kh.edu.istad.iam.domain.valueobject.IdentityId;
import kh.edu.istad.iam.domain.valueobject.UserId;

public class LinkIdentityUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;
    private final CurrentActorProvider currentActorProvider;

    public LinkIdentityUseCase(
            UserRepository userRepository,
            IdentityRepository identityRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = userRepository;
        this.identityRepository = identityRepository;
        this.currentActorProvider = currentActorProvider;
    }

    public IdentityResult execute(LinkIdentityCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("Command is required");
        }

        if (command.userId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (command.provider() == null) {
            throw new IllegalArgumentException("Identity provider is required");
        }

        if (command.providerSubject() == null
                || command.providerSubject().isBlank()) {
            throw new IllegalArgumentException("Provider subject is required");
        }

        User user = userRepository.findById(new UserId(command.userId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        if (user.isDeleted() || !user.isEnabled()) {
            throw new IllegalStateException("User account is not available");
        }

        if (identityRepository.existsByProviderAndSubject(
                command.provider(),
                command.providerSubject()
        )) {
            throw new IllegalStateException(
                    "This external identity is already linked to an account"
            );
        }

        String actor = currentActorProvider.getCurrentActor();

        Identity identity = new Identity(
                IdentityId.generate(),
                user.getId(),
                command.provider(),
                command.providerSubject(),
                command.email(),
                command.emailVerified(),
                command.displayName(),
                command.username(),
                command.pictureUrl(),
                command.attributes(),
                actor
        );

        Identity savedIdentity = identityRepository.save(identity);

        return new IdentityResult(
                savedIdentity.getId().value(),
                savedIdentity.getUserId(),
                savedIdentity.getProvider(),
                savedIdentity.getProviderSubject(),
                savedIdentity.getEmail(),
                savedIdentity.isEmailVerified(),
                savedIdentity.getDisplayName(),
                savedIdentity.getUsername(),
                savedIdentity.getPictureUrl(),
                savedIdentity.getLinkedAt(),
                savedIdentity.getLastLoginAt()
        );
    }
}