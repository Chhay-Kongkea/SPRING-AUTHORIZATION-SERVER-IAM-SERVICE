package kh.edu.istad.iam.domain.command;


import kh.edu.istad.iam.domain.valueobject.IdentityProvider;

import java.util.Map;
import java.util.UUID;

public record LinkIdentityCommand(
        UUID userId,
        IdentityProvider provider,
        String providerSubject,
        String email,
        boolean emailVerified,
        String displayName,
        String username,
        String pictureUrl,
        Map<String, Object> attributes
) {
}