
package kh.edu.istad.iam.infrastructure.security;

import kh.edu.istad.iam.domain.entity.User;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
public class OidcClaimsMapper {

    public Map<String, Object> claims(User user, Set<String> scopes) {
        Map<String, Object> claims = new LinkedHashMap<>();
        claims.put("sub", user.getId().value().toString());

        if (scopes.contains("profile")) {
            put(claims, "name", user.getFullName());
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
            if (!address.isEmpty()) {
                claims.put("address", address);
            }
        }
        return claims;
    }

    private static void put(Map<String, Object> target, String key, Object value) {
        if (value != null) {
            target.put(key, value);
        }
    }
}
