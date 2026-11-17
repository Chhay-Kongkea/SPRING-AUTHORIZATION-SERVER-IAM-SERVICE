package kh.edu.istad.iam.domain.result;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record AuthUserResult(
        UUID userId,
        String username,
        String passwordHash,
        boolean enabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        Set<String> authorities
) {
    public AuthUserResult {
        authorities = authorities == null
                ? new HashSet<>()
                : new HashSet<>(authorities);
    }
}