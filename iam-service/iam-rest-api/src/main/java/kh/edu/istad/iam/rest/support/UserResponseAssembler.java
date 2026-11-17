package kh.edu.istad.iam.rest.support;

import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.usecase.GetUserAuthoritiesUseCase;
import kh.edu.istad.iam.rest.dto.response.UserResponse;
import org.springframework.stereotype.Component;

/** Combines the user and its role assignments into the {@code UserResponse} of spec D.5. */
@Component
public class UserResponseAssembler {

    private final GetUserAuthoritiesUseCase getAuthorities;

    public UserResponseAssembler(GetUserAuthoritiesUseCase getAuthorities) {
        this.getAuthorities = getAuthorities;
    }

    public UserResponse assemble(UserResult user) {
        return ResponseMapper.user(user, getAuthorities.execute(user.id().value()));
    }
}
