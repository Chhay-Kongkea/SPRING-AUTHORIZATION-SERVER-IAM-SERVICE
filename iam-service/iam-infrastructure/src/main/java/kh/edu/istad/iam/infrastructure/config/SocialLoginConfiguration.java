package kh.edu.istad.iam.infrastructure.config;

import kh.edu.istad.iam.infrastructure.social.ConditionalClientRegistrationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SocialLoginConfiguration {

    @Bean
    ClientRegistrationRepository clientRegistrationRepository(
            @Value("${iam.social.google.client-id:}") String googleId,
            @Value("${iam.social.google.client-secret:}") String googleSecret,
            @Value("${iam.social.github.client-id:}") String githubId,
            @Value("${iam.social.github.client-secret:}") String githubSecret,
            @Value("${iam.social.facebook.client-id:}") String facebookId,
            @Value("${iam.social.facebook.client-secret:}") String facebookSecret) {

        List<ClientRegistration> registrations = new ArrayList<>();
        if (!googleId.isBlank() && !googleSecret.isBlank()) {
            registrations.add(ClientRegistration.withRegistrationId("google")
                    .clientId(googleId).clientSecret(googleSecret)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                    .scope("openid", "profile", "email")
                    .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                    .tokenUri("https://oauth2.googleapis.com/token")
                    .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
                    .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                    .userNameAttributeName("sub")
                    .clientName("Google")
                    .build());
        }
        if (!githubId.isBlank() && !githubSecret.isBlank()) {
            registrations.add(ClientRegistration.withRegistrationId("github")
                    .clientId(githubId).clientSecret(githubSecret)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                    .scope("read:user", "user:email")
                    .authorizationUri("https://github.com/login/oauth/authorize")
                    .tokenUri("https://github.com/login/oauth/access_token")
                    .userInfoUri("https://api.github.com/user")
                    .userNameAttributeName("id")
                    .clientName("GitHub")
                    .build());
        }
        if (!facebookId.isBlank() && !facebookSecret.isBlank()) {
            registrations.add(ClientRegistration.withRegistrationId("facebook")
                    .clientId(facebookId).clientSecret(facebookSecret)
                    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                    .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                    .scope("public_profile", "email")
                    .authorizationUri("https://www.facebook.com/v27.0/dialog/oauth")
                    .tokenUri("https://graph.facebook.com/v27.0/oauth/access_token")
                    .userInfoUri("https://graph.facebook.com/me?fields=id,name,email,picture")
                    .userNameAttributeName("id")
                    .clientName("Facebook")
                    .build());
        }
        return new ConditionalClientRegistrationRepository(registrations);
    }
}
