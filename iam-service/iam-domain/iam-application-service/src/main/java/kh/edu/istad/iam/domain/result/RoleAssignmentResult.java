package kh.edu.istad.iam.domain.result;

import java.time.Instant;
import java.util.UUID;

public record RoleAssignmentResult(
        UUID roleUuid,
        String roleName,
        Instant assignedAt,
        Instant expiresAt,
        String assignedBy,
        String reason
) {}
