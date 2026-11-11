package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.GrantPermissionsToUserCommand;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserPermission;
import kh.edu.istad.iam.domain.result.PermissionGrantResult;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.UUID;

public class GrantPermissionsToUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public GrantPermissionsToUserUseCase(
            UserRepository userRepository,
            PermissionRepository permissionRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.permissionRepository = Objects.requireNonNull(permissionRepository);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public List<PermissionGrantResult> execute(GrantPermissionsToUserCommand command) {

        Objects.requireNonNull(command, "Command cannot be null");
        Objects.requireNonNull(command.userId(), "User ID cannot be null");
        Objects.requireNonNull(command.permissionIds(), "Permission IDs cannot be null");
        Objects.requireNonNull(command.effect(), "Permission effect cannot be null");

        if (command.permissionIds().isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one permission must be provided"
            );
        }

        Set<UUID> uniqueIds = new HashSet<>(command.permissionIds());

        if (uniqueIds.size() != command.permissionIds().size()) {
            throw new IllegalArgumentException(
                    "Duplicate permission IDs are not allowed"
            );
        }

        User user = userRepository.findById(
                new UserId(command.userId())
        ).orElseThrow(() -> new NotFoundDomainException(
                "User not found: " + command.userId()
        ));

        for (UUID permissionUuid : command.permissionIds()) {

            Objects.requireNonNull(
                    permissionUuid,
                    "Permission ID cannot be null"
            );

            Permission permission = permissionRepository.findById(
                    new PermissionId(permissionUuid)
            ).orElseThrow(() -> new NotFoundDomainException(
                    "Permission not found: " + permissionUuid
            ));

            if (!permission.isActive()) {
                throw new IllegalStateException(
                        "Cannot grant inactive permission: " + permission.getName()
                );
            }
        }

        String actor = currentActorProvider.getCurrentActor();

        for (UUID permissionUuid : command.permissionIds()) {
            if (command.effect() == PermissionEffect.GRANT) {
                user.grantPermission(new PermissionId(permissionUuid), actor,
                        command.expiresAt(), command.reason());
            } else if (command.effect() == PermissionEffect.DENY) {
                user.denyPermission(new PermissionId(permissionUuid), actor,
                        command.expiresAt(), command.reason());
            } else {
                throw new IllegalArgumentException("Unsupported permission effect");
            }
        }

        User saved = userRepository.save(user);
        return saved.getPermissions().stream()
                .filter(UserPermission::isActive)
                .filter(assignment -> command.permissionIds().contains(assignment.getPermissionId().value()))
                .filter(assignment -> assignment.getEffect() == command.effect())
                .map(assignment -> {
                    Permission permission = permissionRepository.findById(assignment.getPermissionId())
                            .orElseThrow(() -> new NotFoundDomainException("Permission not found"));
                    return new PermissionGrantResult(assignment.getPermissionId().value(), permission.getName(),
                            assignment.getEffect(), assignment.getAssignedAt(), assignment.getExpiresAt(),
                            assignment.getAssignedBy(), assignment.getReason());
                }).toList();
    }
}