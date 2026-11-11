package kh.edu.istad.iam.domain.result;

import java.time.Instant;
import java.util.UUID;

public record PermissionAssignmentResult(
        UUID permissionUuid,
        String permissionName,
        Instant assignedAt,
        Instant expiresAt,
        String assignedBy,
        String reason
) {}
