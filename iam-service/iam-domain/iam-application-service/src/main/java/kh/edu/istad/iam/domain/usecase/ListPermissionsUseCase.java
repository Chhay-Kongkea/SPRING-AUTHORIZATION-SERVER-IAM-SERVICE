package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.result.PermissionResult;

import java.util.List;

public class ListPermissionsUseCase {

    private final PermissionRepository permissionRepository;
    private final IamMapper iamMapper;

    public ListPermissionsUseCase(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public PageResult<PermissionResult> execute(PageQuery query) {

        if (query == null) {
            throw new IllegalArgumentException("Page query is required");
        }

        PageResult<Permission> permissionPage =
                permissionRepository.findAll(query);

        List<PermissionResult> results = permissionPage.content()
                .stream()
                .map(iamMapper::toPermissionResult)
                .toList();

        return new PageResult<>(
                results,
                permissionPage.page(),
                permissionPage.size(),
                permissionPage.totalElements(),
                permissionPage.totalPages(),
                permissionPage.first(),
                permissionPage.last()
        );
    }
}