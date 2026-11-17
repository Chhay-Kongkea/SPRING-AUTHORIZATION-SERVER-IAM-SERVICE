package kh.edu.istad.iam.rest.dto.response;

import kh.edu.istad.iam.domain.valueobject.PermissionEffect;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserAuthoritiesResponse(
        List<RoleGrant> roles,
        List<DirectPermission>
        directPermissions,
        List<String> effectivePermissions) {

    public record RoleGrant(
            UUID uuid,
            String name,
            Instant expiresAt) {
    }

    public record DirectPermission(
            String name,
            PermissionEffect effect) {
    }
}
