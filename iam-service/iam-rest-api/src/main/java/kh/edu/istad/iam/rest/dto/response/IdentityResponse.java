package kh.edu.istad.iam.rest.dto.response;

import kh.edu.istad.iam.domain.valueobject.IdentityProvider;

import java.time.Instant;
import java.util.UUID;

public record IdentityResponse(
        UUID uuid,
        IdentityProvider provider,
        String email,
        String displayName,
        Instant linkedAt) {
}
