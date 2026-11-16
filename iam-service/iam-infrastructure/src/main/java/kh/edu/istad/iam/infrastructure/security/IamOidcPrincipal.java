package kh.edu.istad.iam.infrastructure.security;

import kh.edu.istad.iam.domain.result.AuthUserResult;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;

public final class IamOidcPrincipal extends IamPrincipal implements OidcUser {
    private final OidcIdToken idToken;
    private final OidcUserInfo userInfo;

    public IamOidcPrincipal(AuthUserResult user, Map<String, Object> attributes,
                            OidcIdToken idToken, OidcUserInfo userInfo) {
        super(user, attributes);
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    @Override public Map<String, Object> getClaims() { return idToken.getClaims(); }
    @Override public OidcUserInfo getUserInfo() { return userInfo; }
    @Override public OidcIdToken getIdToken() { return idToken; }
}
