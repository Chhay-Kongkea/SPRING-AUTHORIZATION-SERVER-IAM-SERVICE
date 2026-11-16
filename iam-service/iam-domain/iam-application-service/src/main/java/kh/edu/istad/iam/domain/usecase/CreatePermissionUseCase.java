package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.CreatePermissionCommand;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.result.PermissionResult;
import kh.edu.istad.iam.domain.valueobject.PermissionId;

public class CreatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamMapper iamMapper;

    public CreatePermissionUseCase(
            PermissionRepository permissionRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.permissionRepository = permissionRepository;
        this.currentActorProvider = currentActorProvider;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public PermissionResult execute(CreatePermissionCommand command) {

        if (command == null) {
            throw new IllegalArgumentException(
                    "Create permission command is required"
            );
        }

        if (command.resource() == null || command.resource().isBlank()) {
            throw new IllegalArgumentException("Permission resource is required");
        }

        if (command.action() == null || command.action().isBlank()) {
            throw new IllegalArgumentException("Permission action is required");
        }

        String resource = command.resource().trim().toLowerCase();
        String action = command.action().trim().toLowerCase();

        String permissionName = resource + ":" + action;

        if (permissionRepository.existsByName(permissionName)) {
            throw new IllegalArgumentException(
                    "Permission already exists: " + permissionName
            );
        }

        String actor = currentActorProvider.getCurrentActor();

        Permission permission = new Permission(
                PermissionId.generate(),
                resource,
                action,
                command.description(),
                false,
                actor
        );

        Permission savedPermission = permissionRepository.save(permission);

        return iamMapper.toPermissionResult(savedPermission);
    }
}