package kh.edu.istad.iam.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record RoleId(UUID value) {

    public RoleId {
        Objects.requireNonNull(value, "Role ID cannot be null");
    }

    public static RoleId generate() {
        return new RoleId(UUID.randomUUID());
    }
}