package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.PermissionEffect;

import java.time.Instant;
import java.util.UUID;

public record UserPermissionResult(
        UUID permissionId,
        String permissionName,
        PermissionEffect effect,
        Instant assignedAt,
        String assignedBy,
        Instant expiresAt,
        Instant revokedAt,
        String revokedBy,
        String reason,
        boolean active
) {
}