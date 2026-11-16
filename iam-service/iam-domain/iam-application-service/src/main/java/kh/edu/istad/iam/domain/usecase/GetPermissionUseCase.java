package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.result.PermissionResult;
import kh.edu.istad.iam.domain.valueobject.PermissionId;

import java.util.UUID;

public class GetPermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final IamMapper iamMapper;

    public GetPermissionUseCase(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public PermissionResult execute(UUID permissionUuid) {

        if (permissionUuid == null) {
            throw new IllegalArgumentException("Permission ID is required");
        }

        Permission permission = permissionRepository
                .findById(new PermissionId(permissionUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("Permission not found"));

        return iamMapper.toPermissionResult(permission);
    }
}