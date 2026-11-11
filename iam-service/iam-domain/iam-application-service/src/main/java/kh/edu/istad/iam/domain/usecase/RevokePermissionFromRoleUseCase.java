package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.RevokePermissionFromRoleCommand;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.RoleId;


public class RevokePermissionFromRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    public RevokePermissionFromRoleUseCase(
            RoleRepository roleRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.roleRepository = roleRepository;
        this.currentActorProvider = currentActorProvider;
    }

    public void execute(RevokePermissionFromRoleCommand command) {

        if (command == null) {
            throw new IllegalArgumentException(
                    "Revoke permission command is required"
            );
        }

        if (command.roleId() == null) {
            throw new IllegalArgumentException("Role ID is required");
        }

        if (command.permissionId() == null) {
            throw new IllegalArgumentException("Permission ID is required");
        }

        Role role = roleRepository.findById(new RoleId(command.roleId()))
                .orElseThrow(() ->
                        new IllegalArgumentException("Role not found"));

        String actor = currentActorProvider.getCurrentActor();

        role.revokePermission(
                new PermissionId(command.permissionId()),
                actor
        );

        roleRepository.save(role);
    }
}