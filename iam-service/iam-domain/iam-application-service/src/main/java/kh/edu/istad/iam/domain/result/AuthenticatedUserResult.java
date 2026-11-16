package kh.edu.istad.iam.domain.result;

import java.util.Set;

public record AuthenticatedUserResult(
        String userId,
        String username,
        String passwordHash,
        boolean enabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        Set<String> authorities
) {
}