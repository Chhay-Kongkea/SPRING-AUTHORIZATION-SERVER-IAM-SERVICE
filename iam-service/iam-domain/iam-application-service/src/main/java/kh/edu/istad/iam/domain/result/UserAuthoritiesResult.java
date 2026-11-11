package kh.edu.istad.iam.domain.result;

import java.util.Set;

public record UserAuthoritiesResult(
        String userId,
        Set<String> roles,
        Set<String> permissions
) {
}