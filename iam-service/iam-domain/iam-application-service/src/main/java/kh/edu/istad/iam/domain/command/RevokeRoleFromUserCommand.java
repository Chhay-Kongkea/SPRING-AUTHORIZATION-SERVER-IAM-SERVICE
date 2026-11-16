package kh.edu.istad.iam.domain.command;

import java.util.UUID;

public record RevokeRoleFromUserCommand(
        UUID userId,
        UUID roleId,
        String reason
) {
}