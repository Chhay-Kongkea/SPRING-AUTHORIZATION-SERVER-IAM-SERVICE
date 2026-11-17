package kh.edu.istad.iam.rest.dto.response;

import java.time.Instant;
import java.util.UUID;

public record PermissionResponse(
        UUID uuid,
        String name,
        String resource,
        String action,
        String description,
        boolean system,
        boolean active,
        Instant createdAt) {
}
