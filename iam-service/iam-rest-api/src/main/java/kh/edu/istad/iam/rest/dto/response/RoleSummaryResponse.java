package kh.edu.istad.iam.rest.dto.response;

import java.util.UUID;

public record RoleSummaryResponse(
        UUID uuid,
        String name,
        String description,
        boolean system,
        boolean active) {
}
