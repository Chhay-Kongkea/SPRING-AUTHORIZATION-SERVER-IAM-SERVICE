package kh.edu.istad.iam.rest.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String username,
        String email,
        boolean emailVerified,
        String name,
        String locale,
        boolean enabled,
        List<RoleRef> roles,
        Instant createdAt,
        Instant updatedAt) {

    public record RoleRef(
            UUID uuid,
            String name) {
    }
}
