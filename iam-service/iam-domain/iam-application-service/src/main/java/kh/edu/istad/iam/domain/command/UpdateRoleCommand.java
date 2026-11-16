package kh.edu.istad.iam.domain.command;

import java.util.UUID;

public record UpdateRoleCommand(
        UUID roleId,
        String name,
        String description
) {
}