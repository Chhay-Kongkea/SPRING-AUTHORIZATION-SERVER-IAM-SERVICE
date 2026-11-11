package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.UserAuthoritiesResult;
import kh.edu.istad.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class GetUserAuthoritiesUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public GetUserAuthoritiesUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.roleRepository = Objects.requireNonNull(roleRepository);
        this.permissionRepository = Objects.requireNonNull(permissionRepository);
    }

    public UserAuthoritiesResult execute(UUID userUuid) {

        Objects.requireNonNull(userUuid, "User ID cannot be null");

        User user = userRepository.findById(
                new UserId(userUuid)
        ).orElseThrow(() -> new NotFoundDomainException(
                "User not found: " + userUuid
        ));

        Set<String> roles = new HashSet<>();
        Set<String> grantedPermissions = new HashSet<>();
        Set<String> deniedPermissions = new HashSet<>();

        Instant now = Instant.now();

        user.getRoles().stream()
                .filter(assignment -> assignment.isActive())
                .filter(assignment ->
                        assignment.getExpiresAt() == null
                                || assignment.getExpiresAt().isAfter(now)
                )
                .forEach(assignment -> {

                    Role role = roleRepository.findById(
                            assignment.getRoleId()
                    ).orElse(null);

                    if (role == null || !role.isActive()) {
                        return;
                    }

                    roles.add(role.getName());

                    role.getPermissions().stream()
                            .filter(RolePermission -> RolePermission.isActive())
                            .filter(RolePermission ->
                                    RolePermission.getExpiresAt() == null
                                            || RolePermission.getExpiresAt().isAfter(now)
                            )
                            .forEach(rolePermission -> {

                                Permission permission = permissionRepository.findById(
                                        rolePermission.getPermissionId()
                                ).orElse(null);

                                if (permission != null && permission.isActive()) {
                                    grantedPermissions.add(permission.getName());
                                }
                            });
                });

        user.getPermissions().stream()
                .filter(assignment -> assignment.isActive())
                .filter(assignment ->
                        assignment.getExpiresAt() == null
                                || assignment.getExpiresAt().isAfter(now)
                )
                .forEach(assignment -> {

                    Permission permission = permissionRepository.findById(
                            assignment.getPermissionId()
                    ).orElse(null);

                    if (permission == null || !permission.isActive()) {
                        return;
                    }

                    if (assignment.getEffect() == PermissionEffect.GRANT) {
                        grantedPermissions.add(permission.getName());
                    } else if (assignment.getEffect() == PermissionEffect.DENY) {
                        deniedPermissions.add(permission.getName());
                    }
                });

        grantedPermissions.removeAll(deniedPermissions);

        return new UserAuthoritiesResult(
                user.getId().value().toString(),
                roles,
                grantedPermissions
        );
    }
}