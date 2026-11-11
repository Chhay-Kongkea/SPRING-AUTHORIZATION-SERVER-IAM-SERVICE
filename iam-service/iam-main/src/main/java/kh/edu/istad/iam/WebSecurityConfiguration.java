package kh.edu.istad.iam;

import kh.edu.istad.iam.infrastructure.social.IamOAuth2UserService;
import kh.edu.istad.iam.infrastructure.social.IamOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Bean
    @Order(3)
    SecurityFilterChain webSecurityFilterChain(HttpSecurity http,
            IamOAuth2UserService oauth2UserService,
            IamOidcUserService oidcUserService) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/register", "/auth/telegram/callback", "/oauth2/authorization/**", "/login/oauth2/code/**", "/assets/**", "/css/**", "/images/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login").permitAll())
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(user -> user
                                .userService(oauth2UserService)
                                .oidcUserService(oidcUserService)))
                .logout(logout -> logout.logoutSuccessUrl("/login?logout"));
        return http.build();
    }
}
