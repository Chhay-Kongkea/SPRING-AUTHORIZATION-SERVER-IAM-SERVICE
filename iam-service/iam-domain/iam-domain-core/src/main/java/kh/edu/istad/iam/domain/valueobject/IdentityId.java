package kh.edu.istad.iam.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record IdentityId(UUID value) {

    public IdentityId {
        Objects.requireNonNull(value, "Identity ID cannot be null");
    }

    public static IdentityId generate() {
        return new IdentityId(UUID.randomUUID());
    }
}