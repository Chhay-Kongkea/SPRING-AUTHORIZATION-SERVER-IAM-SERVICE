package kh.edu.istad.iam.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.core.convert.converter.Converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Configuration
@EnableMethodSecurity
public class ApiSecurityConfiguration {

    @Bean
    @Order(2)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/register").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) ->
                                writeProblem(response, 401, "Unauthorized", "A valid Bearer access token is required"))
                        .accessDeniedHandler((request, response, exception) ->
                                writeProblem(response, 403, "Forbidden", "You do not have the required authority")))
                .oauth2ResourceServer(resource -> resource
                        .authenticationEntryPoint((request, response, exception) ->
                                writeProblem(response, 401, "Unauthorized", "A valid Bearer access token is required"))
                        .accessDeniedHandler((request, response, exception) ->
                                writeProblem(response, 403, "Forbidden", "You do not have the required authority"))
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    private void writeProblem(
            jakarta.servlet.http.HttpServletResponse response,
            int status,
            String title,
            String detail
    ) throws java.io.IOException {
        response.setStatus(status);
        response.setContentType("application/problem+json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"type\":\"[https://iam.istad.co/problems/](https://iam.istad.co/problems/)" +
                (status == 401 ? "unauthorized" : "forbidden") +
                "\",\"title\":\"" + title + "\",\"status\":" + status +
                ",\"detail\":\"" + detail + "\"}");
    }

    @Bean
    Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Object claim = jwt.getClaims().get("authorities");
            java.util.stream.Stream<String> permissions = claim instanceof Collection<?> values
                    ? values.stream().filter(String.class::isInstance).map(String.class::cast)
                    : java.util.stream.Stream.empty();

            Object scopeClaim = jwt.getClaims().get("scope");
            java.util.stream.Stream<String> scopes = scopeClaim instanceof String value
                    ? java.util.Arrays.stream(value.split(" "))
                    : java.util.stream.Stream.empty();

            return java.util.stream.Stream.concat(permissions, scopes.map(scope -> "SCOPE_" + scope))
                    .distinct()
                    .map(v -> (GrantedAuthority) new SimpleGrantedAuthority(v))
                    .toList();
        });

        return converter;
    }
}