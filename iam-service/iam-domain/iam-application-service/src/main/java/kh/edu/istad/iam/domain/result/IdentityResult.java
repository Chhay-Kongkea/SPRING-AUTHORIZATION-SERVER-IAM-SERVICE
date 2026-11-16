package kh.edu.istad.iam.domain.result;


import kh.edu.istad.iam.domain.valueobject.IdentityId;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.time.Instant;
import java.util.UUID;

public record IdentityResult(
        UUID id,
        UserId userId,
        IdentityProvider provider,
        String providerSubject,
        String email,
        boolean emailVerified,
        String displayName,
        String username,
        String pictureUrl,
        Instant linkedAt,
        Instant lastLoginAt
) {
}