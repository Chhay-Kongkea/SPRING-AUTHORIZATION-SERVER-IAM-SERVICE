package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.UserId;

import java.time.Instant;

public record UserResult(
        UserId id,
        String username,
        String email,
        boolean emailVerified,
        String givenName,
        String familyName,
        String locale,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
}