package kh.edu.istad.iam.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record PermissionId(UUID value) {

    public PermissionId {
        Objects.requireNonNull(value, "Permission ID cannot be null");
    }

    public static PermissionId generate() {
        return new PermissionId(UUID.randomUUID());
    }
}