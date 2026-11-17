package kh.edu.istad.iam.rest.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class AssignmentRequests {

    private AssignmentRequests() {
    }

    public record RoleAssignments(
            @NotEmpty List<UUID> roleUuids,
            Instant expiresAt,
            String reason
    ) {
    }

    public record PermissionGrants(
            @NotEmpty List<UUID> permissionUuids,
            kh.edu.istad.iam.domain.valueobject.PermissionEffect effect,
            Instant expiresAt,
            String reason
    ) {
    }

    public record RolePermissions(
            @NotEmpty List<UUID> permissionUuids,
            Instant expiresAt,
            String reason
    ) {
    }
}
