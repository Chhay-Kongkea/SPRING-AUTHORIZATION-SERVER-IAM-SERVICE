package kh.edu.istad.iam.domain.command;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AssignRolesToUserCommand(
        UUID userId,
        List<UUID> roleIds,
        Instant expiresAt,
        String reason
) {
}