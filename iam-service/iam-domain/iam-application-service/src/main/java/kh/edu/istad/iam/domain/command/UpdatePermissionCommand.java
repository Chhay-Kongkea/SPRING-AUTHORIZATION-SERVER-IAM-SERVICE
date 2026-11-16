package kh.edu.istad.iam.domain.command;

import java.util.UUID;

public record UpdatePermissionCommand(
        UUID permissionId,
        String resource,
        String action,
        String description
) {
}