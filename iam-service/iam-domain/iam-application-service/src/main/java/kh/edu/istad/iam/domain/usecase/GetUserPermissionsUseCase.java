package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserPermission;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.UserPermissionResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class GetUserPermissionsUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    public GetUserPermissionsUseCase(
            UserRepository userRepository,
            PermissionRepository permissionRepository
    ) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<UserPermissionResult> execute(UUID userUuid) {

        if (userUuid == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        User user = userRepository.findById(new UserId(userUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found: " + userUuid)
                );

        Instant now = Instant.now();

        return user.getPermissions()
                .stream()
                .map(userPermission -> toResult(userPermission, now))
                .toList();
    }

    private UserPermissionResult toResult(
            UserPermission userPermission,
            Instant now
    ) {

        Permission permission = permissionRepository
                .findById(userPermission.getPermissionId())
                .orElse(null);

        boolean active = userPermission.isActive()
                && (userPermission.getExpiresAt() == null
                    || userPermission.getExpiresAt().isAfter(now))
                && permission != null
                && permission.isActive();

        return new UserPermissionResult(
                userPermission.getPermissionId().value(),
                permission != null ? permission.getName() : null,
                userPermission.getEffect(),
                userPermission.getAssignedAt(),
                userPermission.getAssignedBy(),
                userPermission.getExpiresAt(),
                userPermission.getRevokedAt(),
                userPermission.getRevokedBy(),
                userPermission.getReason(),
                active
        );
    }
}