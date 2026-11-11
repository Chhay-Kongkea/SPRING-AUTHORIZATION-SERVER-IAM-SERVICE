package kh.edu.istad.iam.domain.port.out;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(RoleId roleId);

    Optional<Role> findByName(String name);

    boolean existsByName(String name);

    PageResult<Role> findAll(PageQuery query);

}