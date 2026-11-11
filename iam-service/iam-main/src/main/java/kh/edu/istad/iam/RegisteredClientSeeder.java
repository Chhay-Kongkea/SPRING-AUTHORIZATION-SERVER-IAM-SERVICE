package kh.edu.istad.iam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegisteredClientSeeder implements ApplicationRunner {
    private final RegisteredClientRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final String bootstrapId;
    private final String bootstrapSecret;
    private final String webSecret;

    public RegisteredClientSeeder(RegisteredClientRepository repository, PasswordEncoder passwordEncoder,
            @Value("${iam.dcr.initial-client-id}") String bootstrapId,
            @Value("${iam.dcr.initial-client-secret}") String bootstrapSecret,
            @Value("${iam.web-client-secret:change-this-web-client-secret}") String webSecret) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.bootstrapId = bootstrapId;
        this.bootstrapSecret = bootstrapSecret;
        this.webSecret = webSecret;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.findByClientId(bootstrapId) == null) {
            repository.save(RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId(bootstrapId)
                    .clientSecret(passwordEncoder.encode(bootstrapSecret))
                    .clientName("IAM Dynamic Registration Bootstrap")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                    .scope("client.create")
                    .build());
        }

        if (repository.findByClientId("iam-web") == null) {
            repository.save(RegisteredClient.withId(UUID.randomUUID().toString())
                    .clientId("iam-web")
                    .clientSecret(passwordEncoder.encode(webSecret))
                    .clientName("IAM Web Application")
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .redirectUri("http://localhost:8081/login/oauth2/code/iam")
                    .scope("openid").scope("profile").scope("email").scope("phone").scope("address")
                    .clientSettings(ClientSettings.builder()
                            .requireAuthorizationConsent(true)
                            .requireProofKey(true)
                            .build())
                    .build());
        }
    }
}
