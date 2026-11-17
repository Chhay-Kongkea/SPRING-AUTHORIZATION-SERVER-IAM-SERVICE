package kh.edu.istad.iam.infrastructure.social;

import kh.edu.istad.iam.domain.command.FindOrProvisionFederatedUserCommand;
import kh.edu.istad.iam.domain.command.LinkIdentityCommand;
import kh.edu.istad.iam.domain.result.FederatedUserResult;
import kh.edu.istad.iam.domain.usecase.FindOrProvisionFederatedUserUseCase;
import kh.edu.istad.iam.domain.usecase.LinkIdentityUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class FederatedIdentityProvisioner {

    private final FindOrProvisionFederatedUserUseCase findOrProvision;
    private final LinkIdentityUseCase linkIdentity;

    public FederatedIdentityProvisioner(FindOrProvisionFederatedUserUseCase findOrProvision,
                                        LinkIdentityUseCase linkIdentity) {
        this.findOrProvision = findOrProvision;
        this.linkIdentity = linkIdentity;
    }

    public FederatedUserResult provision(IdentityProvider provider, String subject, Map<String, Object> raw) {
        Map<String, Object> attributes = jsonSafe(raw);
        return findOrProvision.execute(new FindOrProvisionFederatedUserCommand(
                provider, subject, string(attributes, "email"), emailVerified(attributes),
                displayName(attributes), username(attributes), picture(attributes), attributes));
    }

    public void link(UUID userId, IdentityProvider provider, String subject, Map<String, Object> raw) {
        Map<String, Object> attributes = jsonSafe(raw);
        linkIdentity.execute(new LinkIdentityCommand(
                userId, provider, subject, string(attributes, "email"), emailVerified(attributes),
                displayName(attributes), username(attributes), picture(attributes), attributes));
    }

    private static boolean emailVerified(Map<String, Object> attributes) {
        return Boolean.parseBoolean(String.valueOf(attributes.getOrDefault("email_verified", false)));
    }

    private static String displayName(Map<String, Object> attributes) {
        String name = string(attributes, "name");
        return name != null ? name : string(attributes, "login");
    }

    private static String username(Map<String, Object> attributes) {
        for (String key : List.of("preferred_username", "login", "username")) {
            String v = string(attributes, key);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    private static String picture(Map<String, Object> attributes) {
        if (attributes.get("picture") instanceof Map<?, ?> picture
                && picture.get("data") instanceof Map<?, ?> data && data.get("url") != null) {
            return data.get("url").toString(); // Facebook
        }
        for (String key : List.of("picture", "avatar_url", "photo_url")) {
            String v = string(attributes, key);
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    private static String string(Map<String, Object> values, String key) {
        Object v = values.get(key);
        return v == null ? null : v.toString();
    }

    /**
     * Provider payloads contain nulls (GitHub) and non-JSON types; both break the
     * immutable copy / jsonb column. Keep only JSON-safe, non-null values.
     */
    static Map<String, Object> jsonSafe(Map<String, Object> source) {
        Map<String, Object> safe = new LinkedHashMap<>();
        if (source == null) {
            return safe;
        }
        source.forEach((key, v) -> {
            Object converted = convert(v);
            if (key != null && converted != null) {
                safe.put(key, converted);
            }
        });
        return safe;
    }

    private static Object convert(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String || v instanceof Number || v instanceof Boolean) {
            return v;
        }
        if (v instanceof Map<?, ?> map) {
            Map<String, Object> nested = new LinkedHashMap<>();
            map.forEach((k, v1) -> {
                Object converted = convert(v1);
                if (k != null && converted != null) {
                    nested.put(k.toString(), converted);
                }
            });
            return nested;
        }
        if (v instanceof Collection<?> collection) {
            List<Object> list = new ArrayList<>();
            collection.forEach(item -> {
                Object converted = convert(item);
                if (converted != null) {
                    list.add(converted);
                }
            });
            return list;
        }
        return v.toString(); // Instant, URL, ...
    }
}
