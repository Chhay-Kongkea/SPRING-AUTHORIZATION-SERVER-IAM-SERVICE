package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.PermissionEffect;

import java.time.Instant;
import java.util.UUID;

public record PermissionGrantResult(
        UUID permissionUuid,
        String permissionName,
        PermissionEffect effect,
        Instant assignedAt,
        Instant expiresAt,
        String assignedBy,
        String reason
) {}
