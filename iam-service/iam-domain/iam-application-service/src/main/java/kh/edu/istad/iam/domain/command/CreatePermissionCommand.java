package kh.edu.istad.iam.domain.command;

public record CreatePermissionCommand(
        String resource,
        String action,
        String description
) {
}