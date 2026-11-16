package kh.edu.istad.iam.infrastructure.security;

import kh.edu.istad.iam.domain.usecase.LoadUserForAuthenticationUseCase;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public final class IamUserDetailsService implements UserDetailsService {
    private final LoadUserForAuthenticationUseCase loadUser;

    public IamUserDetailsService(LoadUserForAuthenticationUseCase loadUser) {
        this.loadUser = loadUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return new IamPrincipal(loadUser.execute(username));
        } catch (RuntimeException ex) {
            throw new UsernameNotFoundException("User not found", ex);
        }
    }
}
