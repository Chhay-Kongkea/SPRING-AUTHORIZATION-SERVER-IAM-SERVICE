package kh.edu.istad.iam.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.edu.istad.iam.domain.command.RegisterUserCommand;
import kh.edu.istad.iam.domain.usecase.RegisterUserUseCase;
import kh.edu.istad.iam.domain.usecase.UpdateUserProfileUseCase;
import kh.edu.istad.iam.domain.usecase.ChangePasswordUseCase;
import kh.edu.istad.iam.domain.usecase.GetUserIdentitiesUseCase;
import kh.edu.istad.iam.domain.usecase.UnlinkIdentityUseCase;
import kh.edu.istad.iam.domain.command.UpdateUserProfileCommand;
import kh.edu.istad.iam.domain.command.ChangePasswordCommand;
import kh.edu.istad.iam.domain.command.UnlinkIdentityCommand;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import org.springframework.context.MessageSource;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import kh.edu.istad.iam.domain.command.ConnectedApplicationsQuery;
import kh.edu.istad.iam.domain.command.RevokeConnectedApplicationCommand;
import kh.edu.istad.iam.domain.usecase.ListConnectedApplicationsUseCase;
import kh.edu.istad.iam.domain.usecase.RevokeConnectedApplicationUseCase;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@Controller
public class WebPageController {

    private final MessageSource messages;
    private final RegisterUserUseCase registerUser;
    private final UpdateUserProfileUseCase updateProfile;
    private final ChangePasswordUseCase changePassword;
    private final GetUserIdentitiesUseCase getIdentities;
    private final UnlinkIdentityUseCase unlinkIdentity;
    private final ListConnectedApplicationsUseCase listConnectedApplications;
    private final RevokeConnectedApplicationUseCase revokeConnectedApplication;
    private final CurrentActorProvider currentActorProvider;
    private final ClientRegistrationRepository clientRegistrations;

    public WebPageController(
            MessageSource messages,
            RegisterUserUseCase registerUser,
            UpdateUserProfileUseCase updateProfile,
            ChangePasswordUseCase changePassword,
            GetUserIdentitiesUseCase getIdentities,
            UnlinkIdentityUseCase unlinkIdentity,
            ListConnectedApplicationsUseCase listConnectedApplications,
            RevokeConnectedApplicationUseCase revokeConnectedApplication,
            CurrentActorProvider currentActorProvider,
            ClientRegistrationRepository clientRegistrations
    ) {
        this.messages = messages;
        this.registerUser = registerUser;
        this.updateProfile = updateProfile;
        this.changePassword = changePassword;
        this.getIdentities = getIdentities;
        this.unlinkIdentity = unlinkIdentity;
        this.listConnectedApplications = listConnectedApplications;
        this.revokeConnectedApplication = revokeConnectedApplication;
        this.currentActorProvider = currentActorProvider;
        this.clientRegistrations = clientRegistrations;
    }

    @GetMapping({"/login"})
    public String login(Model model, HttpServletRequest request) {
        addMessages(model, request,
                "login.title",
                "login.continueWith",
                "register.title",
                "common.logout",
                "common.username",
                "common.password",
                "common.submit",
                "login.invalidCredentials",
                "provider.google",
                "provider.github",
                "provider.facebook",
                "common.english",
                "common.khmer",
                "profile.telegram",
                "profile.telegramEmailNote",
                "profile.telegramEmailRequired",
                "common.email"
        );

        model.addAttribute("error", request.getParameter("error"));
        model.addAttribute("googleEnabled", clientRegistrations.findByRegistrationId("google") != null);
        model.addAttribute("githubEnabled", clientRegistrations.findByRegistrationId("github") != null);
        model.addAttribute("facebookEnabled", clientRegistrations.findByRegistrationId("facebook") != null);

        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, HttpServletRequest request) {
        addMessages(model, request,
                "register.title",
                "common.username",
                "common.email",
                "common.password",
                "common.confirmPassword",
                "common.givenName",
                "common.familyName",
                "common.submit",
                "common.backToLogin",
                "common.english",
                "common.khmer"
        );

        model.addAttribute("errorMessage", null);

        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String givenName,
            @RequestParam String familyName,
            @RequestParam(defaultValue = "en") String locale,
            Model model,
            HttpServletRequest request
    ) {
        if (!password.equals(confirmPassword)) {
            addMessages(model, request,
                    "register.title",
                    "common.username",
                    "common.email",
                    "common.password",
                    "common.confirmPassword",
                    "common.givenName",
                    "common.familyName",
                    "common.submit",
                    "common.backToLogin",
                    "common.english",
                    "common.khmer"
            );

            model.addAttribute("errorMessage", "Password confirmation does not match");

            return "register";
        }

        registerUser.execute(new RegisterUserCommand(
                username,
                email,
                password,
                givenName,
                familyName,
                locale
        ));

        return "redirect:/login?registered";
    }

    @GetMapping("/")
    public String home(
            Model model,
            HttpServletRequest request,
            Authentication authentication
    ) {
        addMessages(model, request,
                "profile.title",
                "profile.linkedAccounts",
                "common.logout",
                "profile.username",
                "profile.authorities",
                "common.english",
                "common.khmer"
        );

        boolean signedIn = authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());

        model.addAttribute("username", signedIn ? authentication.getName() : "");

        model.addAttribute(
                "authorities",
                signedIn
                        ? authentication.getAuthorities().stream()
                        .map(org.springframework.security.core.GrantedAuthority::getAuthority)
                        .toList()
                        : java.util.List.of()
        );

        return "index";
    }

    @GetMapping("/oauth2/consent")
    public String consent(
            Model model,
            HttpServletRequest request,
            @RequestParam(required = false) String client_id,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) java.util.List<String> scope
    ) {
        addMessages(model, request,
                "consent.title",
                "consent.allow",
                "consent.cancel",
                "consent.description",
                "consent.requestedScopes",
                "common.english",
                "common.khmer"
        );

        model.addAttribute("clientId", client_id == null ? "" : client_id);
        model.addAttribute("state", state == null ? "" : state);
        model.addAttribute("scopes", scope == null ? java.util.List.of() : scope);

        return "consent";
    }

    @GetMapping("/profile/edit")
    public String profileEdit(Model model, HttpServletRequest request) {
        addMessages(model, request,
                "profile.edit",
                "common.submit",
                "common.givenName",
                "common.familyName",
                "profile.nickname",
                "profile.locale",
                "common.english",
                "common.khmer"
        );

        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String profileEdit(
            @RequestParam(required = false) String givenName,
            @RequestParam(required = false) String familyName,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String locale,
            Authentication authentication
    ) {
        java.util.UUID userId = java.util.UUID.fromString(
                currentActorProvider.getCurrentActor()
        );

        updateProfile.execute(new UpdateUserProfileCommand(
                userId,
                givenName,
                familyName,
                null,
                nickname,
                null,
                null,
                null,
                null,
                null,
                null,
                locale,
                null,
                null,
                null,
                null,
                null,
                null
        ));

        return "redirect:/";
    }

    @GetMapping("/profile/password")
    public String changePassword(Model model, HttpServletRequest request) {
        addMessages(model, request,
                "profile.changePassword",
                "common.password",
                "common.currentPassword",
                "common.newPassword",
                "common.confirmPassword",
                "common.submit",
                "common.english",
                "common.khmer"
        );

        return "change-password";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @RequestParam String currentPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {
        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password confirmation does not match");
        }

        changePassword.execute(new ChangePasswordCommand(
                java.util.UUID.fromString(currentActorProvider.getCurrentActor()),
                currentPassword,
                newPassword
        ));

        return "redirect:/?passwordChanged";
    }

    @GetMapping("/profile/accounts")
    public String linkedAccounts(Model model, HttpServletRequest request) {
        addMessages(model, request,
                "profile.linkedAccounts",
                "login.continueWith",
                "common.email",
                "profile.linkInstructions",
                "profile.telegram",
                "profile.telegramEmailNote",
                "profile.telegramEmailRequired",
                "common.unlink",
                "provider.google",
                "provider.github",
                "provider.facebook",
                "common.english",
                "common.khmer"
        );

        model.addAttribute(
                "identities",
                getIdentities.execute(
                        java.util.UUID.fromString(currentActorProvider.getCurrentActor())
                )
        );

        model.addAttribute("googleEnabled",
                clientRegistrations.findByRegistrationId("google") != null);
        model.addAttribute("githubEnabled",
                clientRegistrations.findByRegistrationId("github") != null);
        model.addAttribute("facebookEnabled",
                clientRegistrations.findByRegistrationId("facebook") != null);

        return "linked-accounts";
    }

    @PostMapping("/profile/accounts/{identityId}/unlink")
    public String unlinkIdentity(@PathVariable java.util.UUID identityId) {
        unlinkIdentity.execute(new UnlinkIdentityCommand(
                java.util.UUID.fromString(currentActorProvider.getCurrentActor()),
                identityId
        ));

        return "redirect:/profile/accounts";
    }

    @GetMapping("/profile/apps")
    public String connectedApps(
            Model model,
            HttpServletRequest request,
            Authentication authentication
    ) {
        addMessages(model, request,
                "profile.connectedApps",
                "common.revoke",
                "common.english",
                "common.khmer"
        );

        var apps = listConnectedApplications.execute(
                new ConnectedApplicationsQuery(
                        authentication.getName(),
                        currentActorProvider.getCurrentActor()
                )
        );

        model.addAttribute("apps", apps);

        return "connected-apps";
    }

    @PostMapping("/profile/apps/{clientId}/revoke")
    public String revokeConnectedApp(
            Authentication authentication,
            @PathVariable String clientId
    ) {
        revokeConnectedApplication.execute(new RevokeConnectedApplicationCommand(
                clientId,
                authentication.getName(),
                currentActorProvider.getCurrentActor()
        ));

        return "redirect:/profile/apps";
    }

    private void addMessages(Model model, HttpServletRequest request, String... keys) {
        Locale locale = request.getLocale();

        String lang = request.getParameter("lang");

        if ("km".equalsIgnoreCase(lang)) {
            locale = Locale.forLanguageTag("km");
        }

        if ("en".equalsIgnoreCase(lang)) {
            locale = Locale.ENGLISH;
        }

        Map<String, String> resolved = new LinkedHashMap<>();

        for (String key : keys) {
            resolved.put(key, messages.getMessage(key, null, key, locale));
        }

        model.addAttribute("messages", resolved);
        model.addAttribute("csrfToken", "");
        model.addAttribute("csrfParameterName", "_csrf");

        Object csrf = request.getAttribute("_csrf");

        if (csrf instanceof CsrfToken token) {
            model.addAttribute("csrfToken", token.getToken());
            model.addAttribute("csrfParameterName", token.getParameterName());
        }
    }
}