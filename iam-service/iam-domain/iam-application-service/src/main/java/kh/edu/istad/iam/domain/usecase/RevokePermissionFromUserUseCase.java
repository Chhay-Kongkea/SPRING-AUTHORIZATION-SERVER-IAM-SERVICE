package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.RevokePermissionFromUserCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Objects;

public class RevokePermissionFromUserUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;

    public RevokePermissionFromUserUseCase(
            UserRepository userRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public void execute(RevokePermissionFromUserCommand command) {

        Objects.requireNonNull(command, "Command cannot be null");
        Objects.requireNonNull(command.userId(), "User ID cannot be null");
        Objects.requireNonNull(command.permissionId(), "Permission ID cannot be null");
        Objects.requireNonNull(command.effect(), "Permission effect cannot be null");

        if (command.reason() == null || command.reason().isBlank()) {
            throw new IllegalArgumentException("Reason cannot be blank");
        }

        if (command.reason().length() > 255) {
            throw new IllegalArgumentException("Reason cannot exceed 255 characters");
        }

        User user = userRepository.findById(
                new UserId(command.userId())
        ).orElseThrow(() -> new NotFoundDomainException(
                "User not found: " + command.userId()
        ));

        String actor = currentActorProvider.getCurrentActor();

        user.revokePermission(
                new PermissionId(command.permissionId()),
                command.effect(),
                actor,
                command.reason().trim()
        );

        userRepository.save(user);
    }
}