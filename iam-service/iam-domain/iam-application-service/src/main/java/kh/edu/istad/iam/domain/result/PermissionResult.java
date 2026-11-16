package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.PermissionId;

import java.time.Instant;

public record PermissionResult(
        PermissionId id,
        String name,
        String resource,
        String action,
        String description,
        boolean system,
        boolean active,
        Instant createdAt
) {
}