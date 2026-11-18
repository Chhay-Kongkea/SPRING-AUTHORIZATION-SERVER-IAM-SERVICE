
package kh.edu.istad.iam;

import kh.edu.istad.iam.infrastructure.social.IamOAuth2UserService;
import kh.edu.istad.iam.infrastructure.social.IamOidcUserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.net.URI;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Bean
    @Order(3)
    SecurityFilterChain webSecurityFilterChain(
            HttpSecurity http,
            IamOAuth2UserService oauth2UserService,
            IamOidcUserService oidcUserService,
            RequestCache requestCache,
            SecurityContextRepository securityContextRepository
    ) throws Exception {

        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .securityContext(context -> context
                        .securityContextRepository(securityContextRepository)
                )

                .requestCache(cache -> cache
                        .requestCache(requestCache)
                )

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/auth/telegram/callback",
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**",
                                "/assets/**",
                                "/css/**",
                                "/images/**",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler((request, response, authentication) ->
                                loginSuccessRedirect(
                                        request,
                                        response,
                                        authentication,
                                        requestCache,
                                        securityContextRepository
                                )
                        )
                        .permitAll()
                )

                .oauth2Login(oauth -> oauth
                        .loginPage("/login")

                        .successHandler((request, response, authentication) ->
                                loginSuccessRedirect(
                                        request,
                                        response,
                                        authentication,
                                        requestCache,
                                        securityContextRepository
                                )
                        )

                        .userInfoEndpoint(user -> user
                                .userService(oauth2UserService)
                                .oidcUserService(oidcUserService)
                        )
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                );

        return http.build();
    }

    @Bean
    RequestCache requestCache() {
        return new HttpSessionRequestCache();
    }

    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    private void loginSuccessRedirect(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication,
            RequestCache requestCache,
            SecurityContextRepository securityContextRepository
    ) throws IOException {

        // 1. Create an authenticated SecurityContext.
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // 2. Set the SecurityContext for the current request.
        SecurityContextHolder.setContext(context);

        // 3. Persist authentication in the HTTP session.
        securityContextRepository.saveContext(
                context,
                request,
                response
        );

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String target = savedRequest == null ? null : savedRequest.getRedirectUrl();
        requestCache.removeRequest(request, response);

        if (target != null && !isLoginFlowRequest(target, request.getContextPath())) {
            response.sendRedirect(target);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/");
    }

    private boolean isLoginFlowRequest(String target, String contextPath) {
        try {
            String path = URI.create(target).getPath();
            return path == null
                    || path.equals(contextPath + "/login")
                    || path.startsWith(contextPath + "/oauth2/authorization/");
        } catch (IllegalArgumentException invalidTarget) {
            return true;
        }
    }
}
