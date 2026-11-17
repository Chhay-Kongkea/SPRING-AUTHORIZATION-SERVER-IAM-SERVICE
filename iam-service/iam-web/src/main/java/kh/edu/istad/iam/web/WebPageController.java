package kh.edu.istad.iam.web;

import kh.edu.istad.iam.domain.command.ChangePasswordCommand;
import kh.edu.istad.iam.domain.command.ConnectedApplicationsQuery;
import kh.edu.istad.iam.domain.command.RegisterUserCommand;
import kh.edu.istad.iam.domain.command.RevokeConnectedApplicationCommand;
import kh.edu.istad.iam.domain.command.UnlinkIdentityCommand;
import kh.edu.istad.iam.domain.command.UpdateUserProfileCommand;
import kh.edu.istad.iam.domain.exception.ConflictDomainException;
import kh.edu.istad.iam.domain.exception.InvalidCredentialsDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.result.UserAuthoritiesResult;
import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.usecase.ChangePasswordUseCase;
import kh.edu.istad.iam.domain.usecase.GetClientDisplayNameUseCase;
import kh.edu.istad.iam.domain.usecase.GetUserAuthoritiesUseCase;
import kh.edu.istad.iam.domain.usecase.GetUserIdentitiesUseCase;
import kh.edu.istad.iam.domain.usecase.GetUserUseCase;
import kh.edu.istad.iam.domain.usecase.ListConnectedApplicationsUseCase;
import kh.edu.istad.iam.domain.usecase.RegisterUserUseCase;
import kh.edu.istad.iam.domain.usecase.RevokeConnectedApplicationUseCase;
import kh.edu.istad.iam.domain.usecase.UnlinkIdentityUseCase;
import kh.edu.istad.iam.domain.usecase.UpdateUserProfileUseCase;
import kh.edu.istad.iam.infrastructure.security.IamPrincipals;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Controller
public class WebPageController {

    private static final Pattern STRONG_PASSWORD =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{12,128}$");

    private final RegisterUserUseCase registerUser;
    private final GetUserUseCase getUser;
    private final GetUserAuthoritiesUseCase getAuthorities;
    private final UpdateUserProfileUseCase updateProfile;
    private final ChangePasswordUseCase changePassword;
    private final GetUserIdentitiesUseCase getIdentities;
    private final UnlinkIdentityUseCase unlinkIdentity;
    private final ListConnectedApplicationsUseCase listConnectedApplications;
    private final RevokeConnectedApplicationUseCase revokeConnectedApplication;
    private final GetClientDisplayNameUseCase getClientDisplayName;
    private final CurrentActorProvider currentActor;
    private final String telegramBotUsername;

    public WebPageController(RegisterUserUseCase registerUser, GetUserUseCase getUser,
                             GetUserAuthoritiesUseCase getAuthorities, UpdateUserProfileUseCase updateProfile,
                             ChangePasswordUseCase changePassword, GetUserIdentitiesUseCase getIdentities,
                             UnlinkIdentityUseCase unlinkIdentity,
                             ListConnectedApplicationsUseCase listConnectedApplications,
                             RevokeConnectedApplicationUseCase revokeConnectedApplication,
                             GetClientDisplayNameUseCase getClientDisplayName,
                             CurrentActorProvider currentActor,
                             @Value("${iam.telegram.bot-username:}") String telegramBotUsername) {
        this.registerUser = registerUser;
        this.getUser = getUser;
        this.getAuthorities = getAuthorities;
        this.updateProfile = updateProfile;
        this.changePassword = changePassword;
        this.getIdentities = getIdentities;
        this.unlinkIdentity = unlinkIdentity;
        this.listConnectedApplications = listConnectedApplications;
        this.revokeConnectedApplication = revokeConnectedApplication;
        this.getClientDisplayName = getClientDisplayName;
        this.currentActor = currentActor;
        this.telegramBotUsername = telegramBotUsername;
    }

    @GetMapping("/login")
    public String login(
            Model model,
            @RequestParam(name = "error", required = false) String error,
            @RequestParam(name = "logout", required = false) String logout,
            @RequestParam(name = "registered", required = false) String registered
    ) {
        model.addAttribute("error", error != null);
        model.addAttribute("loggedOut", logout != null);
        model.addAttribute("registered", registered != null);
        model.addAttribute("telegramBotUsername", telegramBotUsername);

        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        return registerView(model, Map.of(), Map.of());
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username, @RequestParam("email") String email,
                           @RequestParam("givenName") String givenName, @RequestParam("familyName") String familyName,
                           @RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {
        Map<String, String> values = Map.of("username", username, "email", email,
                "givenName", givenName, "familyName", familyName);
        Map<String, String> errors = new LinkedHashMap<>();
        if (username.isBlank() || username.trim().length() < 3) errors.put("username", "validation.username");
        if (!email.contains("@")) errors.put("email", "validation.email");
        if (givenName.isBlank()) errors.put("givenName", "validation.required");
        if (familyName.isBlank()) errors.put("familyName", "validation.required");
        if (!STRONG_PASSWORD.matcher(password).matches()) errors.put("password", "validation.password");
        if (!password.equals(confirmPassword)) errors.put("confirmPassword", "validation.passwordMismatch");

        if (errors.isEmpty()) {
            try {
                registerUser.execute(new RegisterUserCommand(username, email, password, givenName, familyName, language()));
                return "redirect:/login?registered";
            } catch (ConflictDomainException conflict) {
                errors.put(conflict.getMessage().toLowerCase().contains("username") ? "username" : "email",
                        conflict.getMessage().toLowerCase().contains("username")
                                ? "register.error.usernameTaken" : "register.error.emailTaken");
            } catch (IllegalArgumentException invalid) {
                errors.put("form", "register.error.generic");
            }
        }
        return registerView(model, values, errors);
    }

    private String registerView(Model model, Map<String, String> values, Map<String, String> errors) {
        model.addAttribute("values", values);
        model.addAttribute("errors", errors);
        return "register";
    }

    @GetMapping("/oauth2/consent")
    public String consent(Model model, @RequestParam("client_id") String clientId,
                          @RequestParam("scope") String scope, @RequestParam("state") String state) {
        List<String> scopes = Arrays.stream(scope.trim().split("\\s+")).filter(s -> !s.isBlank()).toList();
        model.addAttribute("clientId", clientId);
        model.addAttribute("clientName", getClientDisplayName.execute(clientId));
        model.addAttribute("state", state);
        model.addAttribute("scopes", scopes);
        return "consent";
    }


    @GetMapping("/")
    public String index(Model model, @RequestParam(name = "saved",required = false) String saved,
                        @RequestParam(name = "passwordChanged", required = false) String passwordChanged) {
        UUID userId = currentUserId();
        UserResult user = getUser.execute(userId);
        UserAuthoritiesResult authorities = getAuthorities.execute(userId);
        Map<String, String> claims = new LinkedHashMap<>();
        claims.put("sub", user.id().value().toString());
        claims.put("preferred_username", user.username());
        claims.put("name", (user.givenName() + " " + user.familyName()).trim());
        claims.put("email", user.email());
        claims.put("email_verified", String.valueOf(user.emailVerified()));
        claims.put("locale", user.locale());
        model.addAttribute("claims", claims);
        model.addAttribute("roles", authorities.roles().stream().sorted().toList());
        model.addAttribute("permissions", authorities.permissions().stream().sorted().toList());
        model.addAttribute("notice", saved != null ? "profile.saved"
                : passwordChanged != null ? "profile.passwordChanged" : "");
        return "index";
    }

    @GetMapping("/profile/edit")
    public String profileEditForm(Model model) {
        UserResult user = getUser.execute(currentUserId());
        model.addAttribute("values", Map.of("givenName", user.givenName(), "familyName", user.familyName(),
                "locale", user.locale()));
        model.addAttribute("errorKey", "");
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String profileEdit(
            @RequestParam(name = "givenName") String givenName,
            @RequestParam(name = "familyName") String familyName,
            @RequestParam(name = "nickname", required = false) String nickname,
            @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(name = "locale") String locale,
            Model model
    ) {
        try {
            updateProfile.execute(new UpdateUserProfileCommand(currentUserId(), blankToNull(givenName),
                    blankToNull(familyName), null, blankToNull(nickname), null, null, null, null, null, null,
                    "km".equals(locale) ? "km" : "en", blankToNull(phoneNumber), null, null, null, null, null));
            return "redirect:/?saved";
        } catch (IllegalArgumentException | ConflictDomainException invalid) {
            model.addAttribute("values", Map.of("givenName", givenName, "familyName", familyName,
                    "nickname", nickname == null ? "" : nickname,
                    "phoneNumber", phoneNumber == null ? "" : phoneNumber, "locale", locale));
            model.addAttribute("errorKey", "validation.profile");
            return "profile-edit";
        }
    }

    @GetMapping("/profile/password")
    public String changePasswordForm(Model model) {
        model.addAttribute("errorKey", "");
        return "change-password";
    }

    @PostMapping("/profile/password")
    public String changePassword(
            @RequestParam(name = "currentPassword") String currentPassword,
            @RequestParam(name = "newPassword") String newPassword,
            @RequestParam(name = "confirmPassword") String confirmPassword,
            Model model
    ) {
        String errorKey = "";
        if (!STRONG_PASSWORD.matcher(newPassword).matches()) {
            errorKey = "validation.password";
        } else if (!newPassword.equals(confirmPassword)) {
            errorKey = "validation.passwordMismatch";
        } else {
            try {
                changePassword.execute(new ChangePasswordCommand(currentUserId(), currentPassword, newPassword));
                return "redirect:/?passwordChanged";
            } catch (InvalidCredentialsDomainException wrong) {
                errorKey = "password.currentIncorrect";
            } catch (IllegalArgumentException same) {
                errorKey = "password.mustDiffer";
            }
        }
        model.addAttribute("errorKey", errorKey);
        return "change-password";
    }

    @GetMapping("/profile/accounts")
    public String linkedAccounts(Model model, @RequestParam(name = "linked",required = false) String linked) {
        model.addAttribute("identities", getIdentities.execute(currentUserId()));
        model.addAttribute("telegramBotUsername", telegramBotUsername);
        model.addAttribute("errorKey", "");
        return "linked-accounts";
    }

    @PostMapping("/profile/accounts/{identityId}/unlink")
    public String unlink(@PathVariable("identityId") UUID identityId, Model model) {
        try {
            unlinkIdentity.execute(new UnlinkIdentityCommand(currentUserId(), identityId));
            return "redirect:/profile/accounts";
        } catch (IllegalStateException lastMethod) {
            model.addAttribute("identities", getIdentities.execute(currentUserId()));
            model.addAttribute("telegramBotUsername", telegramBotUsername);
            model.addAttribute("errorKey", "accounts.lastMethod");
            return "linked-accounts";
        }
    }

    @GetMapping("/profile/apps")
    public String connectedApps(Model model, Authentication authentication) {
        model.addAttribute("apps", listConnectedApplications.execute(
                new ConnectedApplicationsQuery(authentication.getName(), currentActor.getCurrentActor())));
        return "connected-apps";
    }

    @PostMapping("/profile/apps/{clientId}/revoke")
    public String revokeConnectedApp(Authentication authentication, @PathVariable("clientId") String clientId) {
        revokeConnectedApplication.execute(new RevokeConnectedApplicationCommand(
                clientId, authentication.getName(), currentActor.getCurrentActor()));
        return "redirect:/profile/apps";
    }

    private UUID currentUserId() {
        UUID userId = IamPrincipals.currentUserId();

        if (userId == null) {
            throw new IllegalStateException("An authenticated IAM user is required");
        }

        return userId;
    }

    private static String language() {
        return "km".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage()) ? "km" : "en";
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
