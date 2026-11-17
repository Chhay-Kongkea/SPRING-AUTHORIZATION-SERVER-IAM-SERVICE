package kh.edu.istad.iam.rest.support;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

public class CurrentUser {

    private CurrentUser() {
    }


    public static UUID id(Jwt jwt) {
        try {
            return UUID.fromString(jwt.getSubject());
        } catch (IllegalArgumentException | NullPointerException notAUser) {
            throw new AccessDeniedException("This endpoint requires a user access token");
        }
    }
}
