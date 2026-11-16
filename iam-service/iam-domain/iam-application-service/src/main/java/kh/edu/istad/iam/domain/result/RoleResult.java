package kh.edu.istad.iam.domain.result;

import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.time.Instant;
import java.util.Set;

public record RoleResult(
        RoleId id,
        String name,
        String description,
        boolean system,
        boolean active,
        Set<String> permissions,
        Instant createdAt
) {
}