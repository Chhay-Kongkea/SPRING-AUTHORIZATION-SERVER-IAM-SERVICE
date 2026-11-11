package kh.edu.istad.iam.infrastructure.security;

import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.AuthUserResult;
import kh.edu.istad.iam.domain.usecase.LoadUserForAuthenticationUseCase;
import kh.edu.istad.iam.domain.valueobject.UserId;
import org.springframework.security.oauth2.core.oidc.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public final class IamJwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {
    private final UserRepository users;

    public IamJwtCustomizer(UserRepository users) {
        this.users = users;
    }

    @Override
    public void customize(JwtEncodingContext context) {
        if (!(context.getPrincipal().getPrincipal() instanceof IamPrincipal principal)) return;

        context.getClaims().subject(principal.userId().toString());
        User user = users.findById(new UserId(principal.userId())).orElse(null);
        if (user == null) return;

        if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            List<String> authorities = principal.user().authorities().stream().sorted().toList();
            context.getClaims().claim("authorities", authorities);
            context.getClaims().claim("roles", authorities.stream()
                    .filter(value -> value.startsWith("ROLE_"))
                    .map(value -> value.substring(5)).toList());
            context.getClaims().claim("permissions", authorities.stream()
                    .filter(value -> !value.startsWith("ROLE_")).toList());
            return;
        }

        if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
            Map<String, Object> claims = new LinkedHashMap<>();
            var scopes = context.getAuthorizedScopes();
            if (scopes.contains("profile")) {
                put(claims, "name", user.getGivenName() + " " + user.getFamilyName());
                put(claims, "given_name", user.getGivenName());
                put(claims, "family_name", user.getFamilyName());
                put(claims, "middle_name", user.getMiddleName());
                put(claims, "nickname", user.getNickname());
                put(claims, "preferred_username", user.getUsername());
                put(claims, "profile", user.getProfileUrl());
                put(claims, "picture", user.getPictureUrl());
                put(claims, "website", user.getWebsiteUrl());
                put(claims, "gender", user.getGender());
                put(claims, "birthdate", user.getBirthdate() == null ? null : user.getBirthdate().toString());
                put(claims, "zoneinfo", user.getZoneInfo());
                put(claims, "locale", user.getLocale());
                put(claims, "updated_at", user.getUpdatedAt() == null ? null : user.getUpdatedAt().getEpochSecond());
            }
            if (scopes.contains("email")) {
                put(claims, "email", user.getEmail());
                claims.put("email_verified", user.isEmailVerified());
            }
            if (scopes.contains("phone")) {
                put(claims, "phone_number", user.getPhoneNumber());
                claims.put("phone_number_verified", user.isPhoneNumberVerified());
            }
            if (scopes.contains("address")) {
                Map<String, Object> address = new LinkedHashMap<>();
                put(address, "street_address", user.getStreetAddress());
                put(address, "locality", user.getLocality());
                put(address, "region", user.getRegion());
                put(address, "postal_code", user.getPostalCode());
                put(address, "country", user.getCountry());
                claims.put("address", address);
            }
            context.getClaims().claims(existing -> existing.putAll(claims));
        }
    }

    private void put(Map<String, Object> claims, String key, Object value) {
        if (value != null) claims.put(key, value);
    }
}
