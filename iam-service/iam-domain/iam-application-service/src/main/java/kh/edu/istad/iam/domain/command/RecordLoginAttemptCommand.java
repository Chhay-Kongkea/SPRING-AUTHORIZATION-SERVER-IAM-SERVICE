package kh.edu.istad.iam.domain.command;

public record RecordLoginAttemptCommand(
        String usernameOrEmail,
        boolean successful
) {
}