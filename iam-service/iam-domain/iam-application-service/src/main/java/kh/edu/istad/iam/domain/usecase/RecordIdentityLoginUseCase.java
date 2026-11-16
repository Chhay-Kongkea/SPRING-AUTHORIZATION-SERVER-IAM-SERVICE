package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.valueobject.IdentityId;

import java.util.UUID;

public class RecordIdentityLoginUseCase {

    private final IdentityRepository identityRepository;
    private final CurrentActorProvider currentActorProvider;

    public RecordIdentityLoginUseCase(
            IdentityRepository identityRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.identityRepository = identityRepository;
        this.currentActorProvider = currentActorProvider;
    }

    public void execute(UUID identityUuid) {

        if (identityUuid == null) {
            throw new IllegalArgumentException("Identity ID is required");
        }

        Identity identity = identityRepository
                .findById(new IdentityId(identityUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("Identity not found"));

        if (identity.isDeleted()) {
            throw new IllegalArgumentException(
                    "Cannot record login for an unlinked identity"
            );
        }

        identity.recordLogin(currentActorProvider.getCurrentActor());

        identityRepository.save(identity);
    }
}