package kh.edu.istad.iam.domain.command;

import kh.edu.istad.iam.domain.valueobject.PermissionEffect;

import java.util.UUID;

public record RevokePermissionFromUserCommand(
        UUID userId,
        UUID permissionId,
        PermissionEffect effect,
        String reason
) {
}