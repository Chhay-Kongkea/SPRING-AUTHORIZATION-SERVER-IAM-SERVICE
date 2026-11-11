package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.UserId;

public record FederatedUserResult(
        UserId userId,
        String username,
        String email,
        boolean newlyCreated
) {
}