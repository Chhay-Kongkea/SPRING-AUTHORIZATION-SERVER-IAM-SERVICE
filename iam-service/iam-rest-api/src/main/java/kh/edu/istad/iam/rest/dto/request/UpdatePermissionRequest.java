package kh.edu.istad.iam.rest.dto.request;

import jakarta.validation.constraints.Size;

public record UpdatePermissionRequest(
        @Size(max = 50)
        String resource,
        @Size(max = 50)
        String action,
        @Size(max = 255)
        String description) {
}
