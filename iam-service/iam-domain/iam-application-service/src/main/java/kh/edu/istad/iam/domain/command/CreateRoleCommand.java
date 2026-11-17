package kh.edu.istad.iam.domain.command;



import java.util.List;
import java.util.Set;
import java.util.UUID;

public record CreateRoleCommand(
        List<UUID> permissionIds,
        String name,
        String description
) {
}