package kh.edu.istad.iam.rest.dto.response;

import java.util.UUID;

public record UserSummaryResponse(
        UUID uuid,
        String username,
        String email,
        String name,
        boolean enabled,
        boolean locked) {
}
