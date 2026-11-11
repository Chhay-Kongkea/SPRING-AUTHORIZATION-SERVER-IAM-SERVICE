package kh.edu.istad.iam.domain.mapper;

import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.Identity;

import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.result.UserSummaryResult;
import kh.edu.istad.iam.domain.result.RoleResult;
import kh.edu.istad.iam.domain.result.RoleSummaryResult;
import kh.edu.istad.iam.domain.result.PermissionResult;
import kh.edu.istad.iam.domain.result.IdentityResult;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper
public interface IamMapper {

    IamMapper INSTANCE = Mappers.getMapper(IamMapper.class);
    UserResult toUserResult(User user);
    UserSummaryResult toUserSummaryResult(User user);
    RoleSummaryResult toRoleSummaryResult(Role role);

    @Mapping(target = "permissions", source = "permissions")
    RoleResult toRoleResult(Role role, Set<String> permissions);
    PermissionResult toPermissionResult(Permission permission);
    IdentityResult toIdentityResult(Identity identity);

}