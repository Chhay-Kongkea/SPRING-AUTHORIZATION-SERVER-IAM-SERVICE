package kh.edu.istad.iam.rest.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RoleResponse(
        UUID uuid,
        String name,
        String description,
        boolean system,
        boolean active,
        List<String> permissions,
        Instant createdAt) {
}
