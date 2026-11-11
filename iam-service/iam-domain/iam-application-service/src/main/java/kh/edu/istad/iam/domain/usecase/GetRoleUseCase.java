package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.result.RoleResult;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GetRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final IamMapper iamMapper;

    public GetRoleUseCase(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public RoleResult execute(UUID roleUuid) {

        if (roleUuid == null) {
            throw new IllegalArgumentException("Role ID is required");
        }

        Role role = roleRepository.findById(new RoleId(roleUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("Role not found"));

        Set<String> permissions = new HashSet<>();
        Instant now = Instant.now();

        for (RolePermission rolePermission : role.getPermissions()) {

            if (!rolePermission.isActive()) {
                continue;
            }

            if (rolePermission.getExpiresAt() != null
                    && !rolePermission.getExpiresAt().isAfter(now)) {
                continue;
            }

            Permission permission = permissionRepository
                    .findById(rolePermission.getPermissionId())
                    .orElse(null);

            if (permission != null && permission.isActive()) {
                permissions.add(permission.getName());
            }
        }

        return iamMapper.toRoleResult(role, permissions);
    }
}