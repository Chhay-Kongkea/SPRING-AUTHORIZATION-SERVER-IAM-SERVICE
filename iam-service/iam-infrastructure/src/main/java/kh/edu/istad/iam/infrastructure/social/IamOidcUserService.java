package kh.edu.istad.iam.infrastructure.social;

import kh.edu.istad.iam.domain.usecase.LoadUserForAuthenticationUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.infrastructure.security.IamOidcPrincipal;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import kh.edu.istad.iam.infrastructure.security.IamPrincipal;

@Service
public final class IamOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final OidcUserService delegate = new OidcUserService();
    private final FederatedIdentityProvisioner provisioner;
    private final LoadUserForAuthenticationUseCase loadUser;

    public IamOidcUserService(FederatedIdentityProvisioner provisioner,
                              LoadUserForAuthenticationUseCase loadUser) {
        this.provisioner = provisioner;
        this.loadUser = loadUser;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest request) {
        OidcUser external = delegate.loadUser(request);
        Authentication current = SecurityContextHolder.getContext().getAuthentication();
        if (current != null && current.isAuthenticated()
                && current.getPrincipal() instanceof IamPrincipal existing) {
            provisioner.link(existing.userId(), IdentityProvider.GOOGLE,
                    external.getSubject(), external.getClaims());
            return new IamOidcPrincipal(loadUser.execute(existing.getUsername()),
                    external.getClaims(), external.getIdToken(), external.getUserInfo());
        }
        var user = provisioner.provision(IdentityProvider.GOOGLE,
                external.getSubject(), external.getClaims());
        return new IamOidcPrincipal(loadUser.execute(user.username()),
                external.getClaims(), external.getIdToken(), external.getUserInfo());
    }
}
