package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

final class UserLookup {

    private UserLookup() {
    }

    static Optional<User> find(UserRepository users, String identifier) {
        String value = identifier.trim();
        try {
            return users.findById(new UserId(UUID.fromString(value)));
        } catch (IllegalArgumentException notAUuid) {
            String normalized = value.toLowerCase(Locale.ROOT);
            return users.findByUsername(normalized).or(() -> users.findByEmail(normalized));
        }
    }
}
