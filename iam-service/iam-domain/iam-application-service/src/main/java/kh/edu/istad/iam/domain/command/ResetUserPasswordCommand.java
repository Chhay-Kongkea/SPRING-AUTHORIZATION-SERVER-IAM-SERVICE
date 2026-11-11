package kh.edu.istad.iam.domain.command;

import java.util.UUID;

public record ResetUserPasswordCommand(
        UUID userId,
        String newPassword
) {
}