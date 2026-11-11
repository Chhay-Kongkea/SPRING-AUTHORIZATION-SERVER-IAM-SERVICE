package kh.edu.istad.iam.infrastructure.security;

import kh.edu.istad.iam.domain.result.AuthUserResult;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class IamPrincipal implements UserDetails, OAuth2User {
    private final AuthUserResult user;
    private final Map<String, Object> attributes;

    public IamPrincipal(AuthUserResult user) {
        this(user, Map.of());
    }

    public IamPrincipal(AuthUserResult user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = java.util.Collections.unmodifiableMap(new java.util.HashMap<>(attributes));
    }

    public UUID userId() { return user.userId(); }
    public AuthUserResult user() { return user; }

    @Override public Map<String, Object> getAttributes() { return attributes; }
    @Override public String getName() { return user.userId().toString(); }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.authorities().stream().map(SimpleGrantedAuthority::new).toList();
    }
    @Override public String getPassword() { return user.passwordHash(); }
    @Override public String getUsername() { return user.username(); }
    @Override public boolean isAccountNonExpired() { return user.accountNonExpired(); }
    @Override public boolean isAccountNonLocked() { return user.accountNonLocked(); }
    @Override public boolean isCredentialsNonExpired() { return user.credentialsNonExpired(); }
    @Override public boolean isEnabled() { return user.enabled(); }
}
