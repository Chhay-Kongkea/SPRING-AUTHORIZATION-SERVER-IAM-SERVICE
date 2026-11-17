package kh.edu.istad.iam.web;

import jakarta.servlet.http.HttpServletRequest;
import kh.edu.istad.iam.web.i18n.Messages;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Locale;

@ControllerAdvice(basePackageClasses = WebPageController.class)
class ViewModelAdvice {

    private final MessageSource messageSource;
    private final ClientRegistrationRepository registrations;

    ViewModelAdvice(MessageSource messageSource, ClientRegistrationRepository registrations) {
        this.messageSource = messageSource;
        this.registrations = registrations;
    }

    @ModelAttribute("messages")
    Messages messages() {
        return new Messages(messageSource, LocaleContextHolder.getLocale());
    }

    @ModelAttribute("language")
    String language() {
        Locale locale = LocaleContextHolder.getLocale();
        return "km".equalsIgnoreCase(locale.getLanguage()) ? "km" : "en";
    }

    @ModelAttribute("csrfToken")
    String csrfToken(HttpServletRequest request) {
        return request.getAttribute("_csrf") instanceof CsrfToken token ? token.getToken() : "";
    }

    @ModelAttribute("csrfParameterName")
    String csrfParameterName(HttpServletRequest request) {
        return request.getAttribute("_csrf") instanceof CsrfToken token ? token.getParameterName() : "_csrf";
    }

    @ModelAttribute("googleEnabled")
    boolean googleEnabled() {
        return registrations.findByRegistrationId("google") != null;
    }

    @ModelAttribute("githubEnabled")
    boolean githubEnabled() {
        return registrations.findByRegistrationId("github") != null;
    }

    @ModelAttribute("facebookEnabled")
    boolean facebookEnabled() {
        return registrations.findByRegistrationId("facebook") != null;
    }
}
