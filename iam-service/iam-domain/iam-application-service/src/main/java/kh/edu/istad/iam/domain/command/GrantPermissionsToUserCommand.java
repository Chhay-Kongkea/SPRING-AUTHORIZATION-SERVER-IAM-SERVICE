package kh.edu.istad.iam.domain.command;

import kh.edu.istad.iam.domain.valueobject.PermissionEffect;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GrantPermissionsToUserCommand(
        UUID userId,
        List<UUID> permissionIds,
        PermissionEffect effect,
        Instant expiresAt,
        String reason
) {}
