package kh.edu.istad.iam.infrastructure.social;

import kh.edu.istad.iam.domain.command.FindOrProvisionFederatedUserCommand;
import kh.edu.istad.iam.domain.command.LinkIdentityCommand;
import kh.edu.istad.iam.domain.result.FederatedUserResult;
import kh.edu.istad.iam.domain.usecase.FindOrProvisionFederatedUserUseCase;
import kh.edu.istad.iam.domain.usecase.LinkIdentityUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public final class FederatedIdentityProvisioner {
    private final FindOrProvisionFederatedUserUseCase useCase;
    private final LinkIdentityUseCase linkIdentity;

    public FederatedIdentityProvisioner(FindOrProvisionFederatedUserUseCase useCase,
                                        LinkIdentityUseCase linkIdentity) {
        this.useCase = useCase;
        this.linkIdentity = linkIdentity;
    }

    public FederatedUserResult provision(IdentityProvider provider, String subject, Map<String, Object> attributes) {
        String email = string(attributes, "email");
        boolean verified = Boolean.parseBoolean(String.valueOf(attributes.getOrDefault("email_verified", false)));
        String displayName = string(attributes, "name");
        if (displayName == null) displayName = string(attributes, "login");
        String username = string(attributes, "preferred_username");
        if (username == null) username = string(attributes, "login");
        String picture = string(attributes, "picture");
        if (attributes.get("picture") instanceof Map<?, ?> pictureObject
                && pictureObject.get("data") instanceof Map<?, ?> pictureData) {
            Object pictureUrl = pictureData.get("url");
            if (pictureUrl != null) picture = pictureUrl.toString();
        }
        if (picture == null) picture = string(attributes, "avatar_url");

        return useCase.execute(new FindOrProvisionFederatedUserCommand(
                provider, subject, email, verified, displayName, username, picture, attributes));
    }

    public void link(UUID userId, IdentityProvider provider, String subject, Map<String, Object> attributes) {
        String displayName = string(attributes, "name");
        String username = string(attributes, "preferred_username");
        if (username == null) username = string(attributes, "login");
        if (username == null) username = string(attributes, "username");
        String picture = string(attributes, "picture");
        if (attributes.get("picture") instanceof Map<?, ?> pictureObject
                && pictureObject.get("data") instanceof Map<?, ?> pictureData
                && pictureData.get("url") != null) {
            picture = pictureData.get("url").toString();
        }
        if (picture == null) picture = string(attributes, "avatar_url");
        if (picture == null) picture = string(attributes, "photo_url");
        boolean emailVerified = Boolean.parseBoolean(String.valueOf(attributes.getOrDefault("email_verified", false)));
        linkIdentity.execute(new LinkIdentityCommand(userId, provider, subject,
                string(attributes, "email"), emailVerified, displayName, username, picture, attributes));
    }

    private String string(Map<String, Object> values, String key) {
        Object value = values.get(key);
        return value == null ? null : value.toString();
    }
}
