package kh.edu.istad.iam.domain.command;

import kh.edu.istad.iam.domain.valueobject.UserId;

public record RegisterUserCommand(
        String username,
        String email,
        String rawPassword,
        String givenName,
        String familyName,
        String locale

) {
}