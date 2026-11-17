package kh.edu.istad.iam.infrastructure.security;

import kh.edu.istad.iam.domain.result.AuthUserResult;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.List;
import java.util.Map;
import java.util.UUID;

public class IamPrincipals {

    private static final String NO_PASSWORD = "!";

    private IamPrincipals() {
    }

    public static List<GrantedAuthority> authorities(AuthUserResult user) {
        return user.authorities().stream()
                .sorted()
                .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                .toList();
    }

    public static UserDetails userDetails(AuthUserResult user) {
        String hash = user.passwordHash() == null || user.passwordHash().isBlank()
                ? NO_PASSWORD : user.passwordHash();

        return new User(
                user.userId().toString(),
                hash,
                user.enabled(),
                user.accountNonExpired(),
                user.credentialsNonExpired(),
                user.accountNonLocked(),
                authorities(user)
        );
    }

    public static OAuth2User oauth2User(AuthUserResult user) {
        return new DefaultOAuth2User(
                authorities(user),
                Map.of(
                        "sub", user.userId().toString(),
                        "preferred_username", user.username()
                ),
                "sub"
        );
    }

    public static UUID currentUserId() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof IamPrincipal iamPrincipal) {
            return iamPrincipal.userId();
        }

        if (principal instanceof OAuth2User oauth2User) {
            Object subject = oauth2User.getAttribute("sub");

            if (subject != null) {
                try {
                    return UUID.fromString(subject.toString());
                } catch (IllegalArgumentException ignored) {
                    return null;
                }
            }
        }

        try {
            return UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }
}
