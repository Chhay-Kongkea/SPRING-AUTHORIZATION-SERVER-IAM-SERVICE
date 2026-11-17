package kh.edu.istad.iam.rest.support;

import kh.edu.istad.iam.domain.result.IdentityResult;
import kh.edu.istad.iam.domain.result.PermissionResult;
import kh.edu.istad.iam.domain.result.RoleResult;
import kh.edu.istad.iam.domain.result.RoleSummaryResult;
import kh.edu.istad.iam.domain.result.UserAuthoritiesResult;
import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.result.UserSummaryResult;
import kh.edu.istad.iam.rest.dto.response.IdentityResponse;
import kh.edu.istad.iam.rest.dto.response.PermissionResponse;
import kh.edu.istad.iam.rest.dto.response.RoleResponse;
import kh.edu.istad.iam.rest.dto.response.RoleSummaryResponse;
import kh.edu.istad.iam.rest.dto.response.UserAuthoritiesResponse;
import kh.edu.istad.iam.rest.dto.response.UserResponse;
import kh.edu.istad.iam.rest.dto.response.UserSummaryResponse;

import java.util.Comparator;


public class ResponseMapper {

    private ResponseMapper() {
    }

    public static UserResponse user(UserResult user, UserAuthoritiesResult authorities) {
        return new UserResponse(user.id().value(), user.username(), user.email(), user.emailVerified(),
                (user.givenName() + " " + user.familyName()).trim(), user.locale(), user.enabled(),
                authorities.roleGrants().stream()
                        .map(role -> new UserResponse.RoleRef(role.uuid(), role.name()))
                        .sorted(Comparator.comparing(UserResponse.RoleRef::name))
                        .toList(),
                user.createdAt(), user.updatedAt());
    }

    public static UserSummaryResponse userSummary(UserSummaryResult user) {
        return new UserSummaryResponse(user.id().value(), user.username(), user.email(),
                (user.givenName() + " " + user.familyName()).trim(), user.enabled(), user.locked());
    }

    public static UserAuthoritiesResponse authorities(UserAuthoritiesResult result) {
        return new UserAuthoritiesResponse(
                result.roleGrants().stream()
                        .map(role -> new UserAuthoritiesResponse.RoleGrant(role.uuid(), role.name(), role.expiresAt()))
                        .toList(),
                result.directPermissions().stream()
                        .map(p -> new UserAuthoritiesResponse.DirectPermission(p.name(), p.effect()))
                        .toList(),
                result.permissions().stream().sorted().toList());
    }

    public static IdentityResponse identity(IdentityResult identity) {
        return new IdentityResponse(identity.id(), identity.provider(), identity.email(),
                identity.displayName(), identity.linkedAt());
    }

    public static RoleResponse role(RoleResult role) {
        return new RoleResponse(role.id().value(), role.name(), role.description(), role.system(),
                role.active(), role.permissions().stream().sorted().toList(), role.createdAt());
    }

    public static RoleSummaryResponse roleSummary(RoleSummaryResult role) {
        return new RoleSummaryResponse(role.id().value(), role.name(), role.description(),
                role.system(), role.active());
    }

    public static PermissionResponse permission(PermissionResult p) {
        return new PermissionResponse(p.id().value(), p.name(), p.resource(), p.action(), p.description(),
                p.system(), p.active(), p.createdAt());
    }
}
