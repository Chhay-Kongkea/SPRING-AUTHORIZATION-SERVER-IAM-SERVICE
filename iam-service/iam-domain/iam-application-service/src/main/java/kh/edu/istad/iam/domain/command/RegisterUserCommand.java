package kh.edu.istad.iam.domain.command;

public record RegisterUserCommand(
        String username,
        String email,
        String rawPassword,
        String givenName,
        String familyName,
        String locale

) {
}