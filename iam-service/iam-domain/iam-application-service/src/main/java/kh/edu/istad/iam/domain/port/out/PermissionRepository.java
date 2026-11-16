package kh.edu.istad.iam.domain.port.out;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.valueobject.PermissionId;

import java.util.Optional;

public interface PermissionRepository {

    Permission save(Permission permission);

    Optional<Permission> findById(PermissionId permissionId);

    Optional<Permission> findByName(String name);

    boolean existsByName(String name);

    PageResult<Permission> findAll(PageQuery query);
}