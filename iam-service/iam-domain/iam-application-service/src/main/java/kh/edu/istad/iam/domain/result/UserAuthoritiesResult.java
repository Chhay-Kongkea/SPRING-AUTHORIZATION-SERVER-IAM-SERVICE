package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.PermissionEffect;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public record UserAuthoritiesResult(
        String userId,
        Set<String> roles,
        Set<String> permissions,
        List<RoleGrant> roleGrants,
        List<DirectPermission> directPermissions
) {
    public record RoleGrant(
            UUID uuid,
            String name,
            Instant expiresAt) {
    }

    public record DirectPermission(
            UUID uuid,
            String name,
            PermissionEffect effect,
            Instant expiresAt) {
    }
}
