package kh.edu.istad.iam.infrastructure.config;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jose.proc.SecurityContext;
import kh.edu.istad.iam.infrastructure.security.OidcClaimsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.valueobject.UserId;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2AccessTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.http.MediaType;
import com.nimbusds.jose.jwk.source.JWKSource;

import java.util.UUID;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;

@Configuration
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1)
    SecurityFilterChain authorizationServerSecurityFilterChain(
            HttpSecurity http, UserRepository users,
            RegisteredClientRepository registeredClients,
            OAuth2AuthorizationService authorizationService,
            OAuth2AuthorizationConsentService consentService,
            AuthorizationServerSettings serverSettings,
            OAuth2TokenGenerator<?> tokenGenerator,
            OidcClaimsMapper claimsMapper) throws Exception {
        http.oauth2AuthorizationServer(server -> {
                    http.securityMatcher(server.getEndpointsMatcher());
                    server.registeredClientRepository(registeredClients)
                            .authorizationService(authorizationService)
                            .authorizationConsentService(consentService)
                            .authorizationServerSettings(serverSettings)
                            .tokenGenerator(tokenGenerator);
                    server.authorizationEndpoint(
                            endpoint -> endpoint.consentPage("/oauth2/consent"));
                    server.oidc(oidc -> oidc
                            .clientRegistrationEndpoint(Customizer.withDefaults())
                            .userInfoEndpoint(endpoint ->
                                    endpoint.userInfoMapper(context ->
                                    mapUserInfo(context, users, claimsMapper))));
                })
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()))
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));
        return http.build();
    }


    private OidcUserInfo mapUserInfo(OidcUserInfoAuthenticationContext context, UserRepository users,
                                     OidcClaimsMapper claimsMapper) {
        User user = users.findById(new UserId(UUID.fromString(context.getAuthorization().getPrincipalName())))
                .orElseThrow(() -> new IllegalStateException("Authorized IAM user no longer exists"));
        return new OidcUserInfo(claimsMapper.claims(user, context.getAuthorization().getAuthorizedScopes()));
    }

    @Bean
    RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    OAuth2AuthorizationService authorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository clients) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, clients);
    }

    @Bean
    OAuth2AuthorizationConsentService authorizationConsentService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository clients) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, clients);
    }

    @Bean
    JWKSource<SecurityContext> jwkSource(
            @Value("${iam.keys.public-key-b64:}") String publicKeyBase64,
            @Value("${iam.keys.private-key-b64:}") String privateKeyBase64,
            @Value("${iam.keys.allow-ephemeral:false}") boolean allowEphemeral) throws Exception {
        RSAKey rsaKey;
        if (publicKeyBase64.isBlank() != privateKeyBase64.isBlank()) {
            throw new IllegalStateException("Configure both IAM_JWK_PUBLIC_KEY_B64 and IAM_JWK_PRIVATE_KEY_B64");
        }
        if (!publicKeyBase64.isBlank()) {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] publicBytes = Base64.getDecoder().decode(publicKeyBase64);
            byte[] privateBytes = Base64.getDecoder().decode(privateKeyBase64);
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(
                    new X509EncodedKeySpec(publicBytes));
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(
                    new PKCS8EncodedKeySpec(privateBytes));
            rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey)
                    .keyID("iam-signing-key-1").build();
        } else {
            if (!allowEphemeral) {
                throw new IllegalStateException("A persistent RSA signing key pair is required");
            }

            rsaKey = new RSAKeyGenerator(2048).keyID("iam-dev-ephemeral-key").generate();
        }
        return new ImmutableJWKSet<>(new JWKSet(rsaKey));
    }


    @Bean
    JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    OAuth2TokenGenerator<?> tokenGenerator(JwtEncoder jwtEncoder,
                                           OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer) {
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(jwtCustomizer);
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator,
                new OAuth2AccessTokenGenerator(),
                new OAuth2RefreshTokenGenerator());
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings(
            @Value("${iam.issuer:http://localhost:9000}") String issuer) {
        return AuthorizationServerSettings.builder().issuer(issuer).build();
    }
}
