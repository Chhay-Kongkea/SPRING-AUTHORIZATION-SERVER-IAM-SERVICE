package kh.edu.istad.iam.infrastructure.adapter;

import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import org.springframework.security.core.Authentication;
import kh.edu.istad.iam.infrastructure.security.IamPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class SecurityCurrentActorProvider implements CurrentActorProvider {
    @Override
    public String getCurrentActor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return "system";
        }
        if (authentication.getPrincipal() instanceof IamPrincipal principal) {
            return principal.userId().toString();
        }
        return authentication.getName();
    }
}
