package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.AuthUserResult;
import kh.edu.istad.iam.domain.result.UserAuthoritiesResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.HashSet;
import java.util.Set;

public class LoadUserForAuthenticationUseCase {

    private final UserRepository userRepository;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;

    public LoadUserForAuthenticationUseCase(
            UserRepository userRepository,
            GetUserAuthoritiesUseCase getUserAuthoritiesUseCase
    ) {
        this.userRepository = userRepository;
        this.getUserAuthoritiesUseCase = getUserAuthoritiesUseCase;
    }

    public AuthUserResult execute(String usernameOrEmail) {

        if (usernameOrEmail == null || usernameOrEmail.isBlank()) {
            throw new IllegalArgumentException(
                    "Username or email is required"
            );
        }

        String identifier = usernameOrEmail.trim();

        User user = userRepository.findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        if (user.isDeleted()) {
            throw new IllegalStateException("User account is deleted");
        }

        UserAuthoritiesResult userAuthorities =
                getUserAuthoritiesUseCase.execute(
                        user.getId().value()
                );

        Set<String> authorities = new HashSet<>();

        userAuthorities.roles().forEach(
                role -> authorities.add("ROLE_" + role)
        );

        authorities.addAll(userAuthorities.permissions());

        return new AuthUserResult(
                user.getId().value(),
                user.getUsername(),
                user.getPasswordHash(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                authorities
        );
    }
}