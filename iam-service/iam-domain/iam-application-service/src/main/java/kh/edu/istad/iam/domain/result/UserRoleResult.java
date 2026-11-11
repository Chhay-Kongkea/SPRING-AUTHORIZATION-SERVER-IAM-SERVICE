package kh.edu.istad.iam.domain.result;

import java.time.Instant;
import java.util.UUID;

public record UserRoleResult(
        UUID roleId,
        String roleName,
        Instant assignedAt,
        String assignedBy,
        Instant expiresAt,
        Instant revokedAt,
        String revokedBy,
        String reason,
        boolean active
) {
}