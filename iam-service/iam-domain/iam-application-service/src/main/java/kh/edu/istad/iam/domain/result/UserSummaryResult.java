package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.UserId;

public record UserSummaryResult(
        UserId id,
        String username,
        String email,
        String givenName,
        String familyName,
        boolean enabled,
        boolean locked
) {
}