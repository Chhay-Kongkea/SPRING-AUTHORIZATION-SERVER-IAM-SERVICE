package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.UnlinkIdentityCommand;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.valueobject.IdentityId;
import kh.edu.istad.iam.domain.valueobject.UserId;

public class UnlinkIdentityUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;
    private final CurrentActorProvider currentActorProvider;

    public UnlinkIdentityUseCase(
            UserRepository userRepository,
            IdentityRepository identityRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = userRepository;
        this.identityRepository = identityRepository;
        this.currentActorProvider = currentActorProvider;
    }

    public void execute(UnlinkIdentityCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("Command is required");
        }

        if (command.userId() == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        if (command.identityId() == null) {
            throw new IllegalArgumentException("Identity ID is required");
        }

        User user = userRepository.findById(new UserId(command.userId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        if (user.isDeleted()) {
            throw new IllegalStateException("Deleted user cannot unlink identities");
        }

        Identity identity = identityRepository.findById(
                        new IdentityId(command.identityId())
                )
                .orElseThrow(() ->
                        new IllegalArgumentException("Identity not found")
                );

        if (!identity.getUserId().equals(user.getId())) {
            throw new IllegalStateException(
                    "Identity does not belong to this user"
            );
        }

        if (identity.isDeleted()) {
            throw new IllegalStateException("Identity is already unlinked");
        }

        long activeIdentities = identityRepository
                .countActiveByUserId(user.getId());

        boolean hasPassword = user.getPasswordHash() != null
                && !user.getPasswordHash().isBlank();

        if (activeIdentities <= 1 && !hasPassword) {
            throw new IllegalStateException(
                    "Cannot unlink the last sign-in method"
            );
        }

        String actor = currentActorProvider.getCurrentActor();

        identity.unlink(actor);

        identityRepository.save(identity);
    }
}