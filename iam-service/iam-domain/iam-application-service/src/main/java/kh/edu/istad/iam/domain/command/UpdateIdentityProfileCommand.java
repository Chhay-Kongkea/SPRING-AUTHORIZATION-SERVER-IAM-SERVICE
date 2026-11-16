package kh.edu.istad.iam.domain.command;

import java.util.Map;
import java.util.UUID;

public record UpdateIdentityProfileCommand(
        UUID identityId,
        String email,
        boolean emailVerified,
        String displayName,
        String username,
        String pictureUrl,
        Map<String, Object> attributes
) {
}