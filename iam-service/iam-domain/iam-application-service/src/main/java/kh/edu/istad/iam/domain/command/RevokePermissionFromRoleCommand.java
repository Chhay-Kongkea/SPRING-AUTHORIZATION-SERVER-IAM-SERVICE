package kh.edu.istad.iam.domain.command;

import java.util.UUID;

public record RevokePermissionFromRoleCommand(
        UUID roleId,
        UUID permissionId,
        String reason
) {
}