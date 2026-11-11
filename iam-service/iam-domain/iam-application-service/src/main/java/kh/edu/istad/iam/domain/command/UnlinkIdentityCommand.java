package kh.edu.istad.iam.domain.command;

import java.util.UUID;

public record UnlinkIdentityCommand(
        UUID userId,
        UUID identityId
) {
}