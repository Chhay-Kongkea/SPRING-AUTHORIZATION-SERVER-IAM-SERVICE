package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.UpdateIdentityProfileCommand;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.result.IdentityResult;
import kh.edu.istad.iam.domain.valueobject.IdentityId;

public class UpdateIdentityProfileUseCase {

    private final IdentityRepository identityRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamMapper iamMapper;

    public UpdateIdentityProfileUseCase(
            IdentityRepository identityRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.identityRepository = identityRepository;
        this.currentActorProvider = currentActorProvider;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public IdentityResult execute(UpdateIdentityProfileCommand command) {

        if (command == null) {
            throw new IllegalArgumentException(
                    "Update identity profile command is required"
            );
        }

        if (command.identityId() == null) {
            throw new IllegalArgumentException("Identity ID is required");
        }

        Identity identity = identityRepository
                .findById(new IdentityId(command.identityId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Identity not found"));

        if (identity.isDeleted()) {
            throw new IllegalArgumentException("Identity has already been unlinked");
        }

        String actor = currentActorProvider.getCurrentActor();

        identity.updateProviderProfile(
                command.email(),
                command.emailVerified(),
                command.displayName(),
                command.username(),
                command.pictureUrl(),
                command.attributes(),
                actor
        );

        Identity savedIdentity = identityRepository.save(identity);

        return iamMapper.toIdentityResult(savedIdentity);
    }
}