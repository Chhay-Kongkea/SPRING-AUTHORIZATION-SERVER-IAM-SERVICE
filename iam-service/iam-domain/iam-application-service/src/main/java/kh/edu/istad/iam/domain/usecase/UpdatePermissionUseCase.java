package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.UpdatePermissionCommand;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.result.PermissionResult;
import kh.edu.istad.iam.domain.valueobject.PermissionId;

import java.util.UUID;

public class UpdatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamMapper iamMapper;

    public UpdatePermissionUseCase(
            PermissionRepository permissionRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.permissionRepository = permissionRepository;
        this.currentActorProvider = currentActorProvider;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public PermissionResult execute(UpdatePermissionCommand command) {

        if (command == null) {
            throw new IllegalArgumentException(
                    "Update permission command is required"
            );
        }

        if (command.permissionId() == null) {
            throw new IllegalArgumentException("Permission ID is required");
        }

        if (command.resource() == null || command.resource().isBlank()) {
            throw new IllegalArgumentException("Permission resource is required");
        }

        if (command.action() == null || command.action().isBlank()) {
            throw new IllegalArgumentException("Permission action is required");
        }

        UUID permissionUuid = command.permissionId();

        String resource = command.resource().trim().toLowerCase();
        String action = command.action().trim().toLowerCase();
        String name = resource + ":" + action;

        Permission permission = permissionRepository
                .findById(new PermissionId(permissionUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("Permission not found"));

        if (!permission.getName().equals(name)
                && permissionRepository.existsByName(name)) {
            throw new IllegalArgumentException(
                    "Permission already exists: " + name
            );
        }

        String actor = currentActorProvider.getCurrentActor();

        permission.updateDetails(resource, action, command.description(), actor);

        Permission savedPermission = permissionRepository.save(permission);

        return iamMapper.toPermissionResult(savedPermission);
    }
}