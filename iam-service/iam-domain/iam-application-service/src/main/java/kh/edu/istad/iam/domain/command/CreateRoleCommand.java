package kh.edu.istad.iam.domain.command;

public record CreateRoleCommand(
        String name,
        String description
) {
}