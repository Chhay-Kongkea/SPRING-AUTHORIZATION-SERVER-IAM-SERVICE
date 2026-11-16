package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.AssignPermissionsToRoleCommand;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.domain.result.PermissionAssignmentResult;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.UUID;

public class AssignPermissionsToRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public AssignPermissionsToRoleUseCase(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.currentActorProvider = currentActorProvider;
    }

    public List<PermissionAssignmentResult> execute(AssignPermissionsToRoleCommand command) {

        if (command == null) {
            throw new IllegalArgumentException(
                    "Assign permissions command is required"
            );
        }

        if (command.roleId() == null) {
            throw new IllegalArgumentException("Role ID is required");
        }

        if (command.permissionIds() == null || command.permissionIds().isEmpty()) {
            throw new IllegalArgumentException("Permission IDs are required");
        }

        Set<UUID> uniquePermissionIds = new HashSet<>(command.permissionIds());

        if (uniquePermissionIds.size() != command.permissionIds().size()) {
            throw new IllegalArgumentException("Duplicate permission IDs are not allowed");
        }

        Role role = roleRepository.findById(new RoleId(command.roleId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Role not found"));

        String actor = currentActorProvider.getCurrentActor();

        for (UUID permissionUuid : command.permissionIds()) {

            if (permissionUuid == null) {
                throw new IllegalArgumentException("Permission ID cannot be null");
            }

            PermissionId permissionId = new PermissionId(permissionUuid);

            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Permission not found: " + permissionUuid));

            if (!permission.isActive() || permission.isDeleted()) {
                throw new IllegalArgumentException(
                        "Cannot assign inactive or deleted permission: " + permissionUuid
                );
            }

            role.assignPermission(
                    permissionId,
                    actor,
                    command.expiresAt(),
                    command.reason()
            );
        }

        Role saved = roleRepository.save(role);
        return saved.getPermissions().stream()
                .filter(RolePermission::isActive)
                .filter(assignment -> command.permissionIds().contains(assignment.getPermissionId().value()))
                .map(assignment -> {
                    Permission permission = permissionRepository.findById(assignment.getPermissionId())
                            .orElseThrow(() -> new IllegalArgumentException("Permission not found"));
                    return new PermissionAssignmentResult(assignment.getPermissionId().value(),
                            permission.getName(), assignment.getAssignedAt(), assignment.getExpiresAt(),
                            assignment.getAssignedBy(), assignment.getReason());
                }).toList();
    }
}