package kh.edu.istad.iam.infrastructure.social;

import kh.edu.istad.iam.domain.usecase.LoadUserForAuthenticationUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.infrastructure.security.IamPrincipal;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import kh.edu.istad.iam.infrastructure.security.IamPrincipal;
import org.springframework.web.client.RestClient;
import org.springframework.core.ParameterizedTypeReference;
import java.util.HashMap;
import java.util.List;

import java.util.Map;

@Service
public final class IamOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final FederatedIdentityProvisioner provisioner;
    private final LoadUserForAuthenticationUseCase loadUser;
    private final RestClient.Builder restClientBuilder;

    public IamOAuth2UserService(FederatedIdentityProvisioner provisioner,
                                LoadUserForAuthenticationUseCase loadUser, RestClient.Builder restClientBuilder) {
        this.provisioner = provisioner;
        this.loadUser = loadUser;
        this.restClientBuilder = restClientBuilder;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User external = delegate.loadUser(request);
        IdentityProvider provider = IdentityProvider.valueOf(
                request.getClientRegistration().getRegistrationId().toUpperCase());
        Map<String, Object> attributes = new HashMap<>(external.getAttributes());
        if (provider == IdentityProvider.GITHUB && attributes.get("email") == null) {
            List<Map<String, Object>> emails = restClientBuilder.build().get()
                    .uri("https://api.github.com/user/emails")
                    .header("Authorization", "Bearer " + request.getAccessToken().getTokenValue())
                    .header("Accept", "application/vnd.github+json")
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            if (emails != null) {
                emails.stream()
                        .filter(item -> Boolean.TRUE.equals(item.get("primary"))
                                && Boolean.TRUE.equals(item.get("verified")))
                        .findFirst()
                        .ifPresent(item -> {
                            attributes.put("email", item.get("email"));
                            attributes.put("email_verified", true);
                        });
            }
        }
        Object subjectValue = attributes.getOrDefault("id", attributes.get("sub"));
        if (subjectValue == null) throw new IllegalArgumentException("Provider subject is missing");
        Authentication current = SecurityContextHolder.getContext().getAuthentication();
        if (current != null && current.isAuthenticated()
                && current.getPrincipal() instanceof IamPrincipal existing) {
            provisioner.link(existing.userId(), provider, subjectValue.toString(), attributes);
            return new IamPrincipal(loadUser.execute(existing.getUsername()), attributes);
        }
        var user = provisioner.provision(provider, subjectValue.toString(), attributes);
        return new IamPrincipal(loadUser.execute(user.username()), attributes);
    }
}
