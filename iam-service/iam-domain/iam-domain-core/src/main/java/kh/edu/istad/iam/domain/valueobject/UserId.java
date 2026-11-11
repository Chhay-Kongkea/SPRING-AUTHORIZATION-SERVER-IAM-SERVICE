package kh.edu.istad.iam.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

    public UserId {
        Objects.requireNonNull(value, "User ID cannot be null");
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
}