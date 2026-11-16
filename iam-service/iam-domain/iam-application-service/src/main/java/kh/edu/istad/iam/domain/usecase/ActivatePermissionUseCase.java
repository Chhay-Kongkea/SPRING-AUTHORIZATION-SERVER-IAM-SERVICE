package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.valueobject.PermissionId;

import java.util.UUID;

public class ActivatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public ActivatePermissionUseCase(
            PermissionRepository permissionRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.permissionRepository = permissionRepository;
        this.currentActorProvider = currentActorProvider;
    }

    public void execute(UUID permissionUuid) {

        if (permissionUuid == null) {
            throw new IllegalArgumentException("Permission ID is required");
        }

        Permission permission = permissionRepository
                .findById(new PermissionId(permissionUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("Permission not found"));

        String actor = currentActorProvider.getCurrentActor();

        permission.activate(actor);

        permissionRepository.save(permission);
    }
}