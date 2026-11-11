package kh.edu.istad.iam.domain.command;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AssignPermissionsToRoleCommand(
        UUID roleId,
        List<UUID> permissionIds,
        Instant expiresAt,
        String reason
) {
}