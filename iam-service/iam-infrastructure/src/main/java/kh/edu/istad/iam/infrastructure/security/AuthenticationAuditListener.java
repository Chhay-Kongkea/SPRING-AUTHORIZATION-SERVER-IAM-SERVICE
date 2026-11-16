package kh.edu.istad.iam.infrastructure.security;

import kh.edu.istad.iam.domain.command.RecordLoginAttemptCommand;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.usecase.RecordLoginAttemptUseCase;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public final class AuthenticationAuditListener {
    private final RecordLoginAttemptUseCase recordLoginAttempt;

    public AuthenticationAuditListener(RecordLoginAttemptUseCase recordLoginAttempt) {
        this.recordLoginAttempt = recordLoginAttempt;
    }

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof IamPrincipal iamPrincipal) {
            recordLoginAttempt.execute(new RecordLoginAttemptCommand(iamPrincipal.getUsername(), true));
        }
    }

    @EventListener
    public void onAuthenticationFailure(AbstractAuthenticationFailureEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof String username && !username.isBlank()) {
            try {
                recordLoginAttempt.execute(new RecordLoginAttemptCommand(username, false));
            } catch (NotFoundDomainException ignored) {
                // Do not disclose whether an account exists.
            }
        }
    }
}
