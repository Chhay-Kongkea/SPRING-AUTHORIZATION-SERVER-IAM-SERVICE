package kh.edu.istad.iam.infrastructure.social;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kh.edu.istad.iam.domain.usecase.LoadUserForAuthenticationUseCase;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.infrastructure.security.IamPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class TelegramLoginController {
    private final String botToken;
    private final FederatedIdentityProvisioner provisioner;
    private final LoadUserForAuthenticationUseCase loadUser;
    private final JdbcTemplate jdbcTemplate;

    public TelegramLoginController(@Value("${iam.telegram.bot-token:}") String botToken,
            FederatedIdentityProvisioner provisioner,
            LoadUserForAuthenticationUseCase loadUser, JdbcTemplate jdbcTemplate) {
        this.botToken = botToken;
        this.provisioner = provisioner;
        this.loadUser = loadUser;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/auth/telegram/callback")
    public String callback(@RequestParam Map<String, String> data,
            HttpServletRequest request, HttpServletResponse response) {
        if (botToken.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Telegram login is not configured");
        }
        String suppliedHash = data.get("hash");
        String authDate = data.get("auth_date");
        String id = data.get("id");
        String email = data.get("email");
        if (suppliedHash == null || authDate == null || id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Telegram authentication payload is incomplete");
        }
        long timestamp;
        try {
            timestamp = Long.parseLong(authDate);
        } catch (NumberFormatException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Telegram auth_date");
        }
        long age = Instant.now().getEpochSecond() - timestamp;
        if (age < -60 || age > 86_400) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Telegram login data has expired");
        }
        if (!MessageDigest.isEqual(
                suppliedHash.getBytes(StandardCharsets.UTF_8),
                telegramHash(data).getBytes(StandardCharsets.UTF_8))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Telegram signature");
        }

        jdbcTemplate.update("DELETE FROM telegram_auth_replays WHERE used_at < CURRENT_TIMESTAMP - INTERVAL '1 day'");
        try {
            jdbcTemplate.update("INSERT INTO telegram_auth_replays(telegram_hash) VALUES (?)", suppliedHash);
        } catch (DuplicateKeyException replay) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Telegram login payload was already used");
        }

        Authentication current = SecurityContextHolder.getContext().getAuthentication();
        boolean linking = current != null && current.isAuthenticated()
                && current.getPrincipal() instanceof IamPrincipal;
        if (!linking && (email == null || email.isBlank())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "A user-provided email address is required for a new Telegram account");
        }

        Map<String, Object> attributes = new TreeMap<>(data);
        if (email != null && !email.isBlank()) attributes.put("email", email);
        attributes.put("email_verified", false);
        String displayName = (data.getOrDefault("first_name", "") + " "
                + data.getOrDefault("last_name", "")).trim();
        attributes.put("name", displayName.isBlank() ? "Telegram user " + id : displayName);
        if (data.get("username") != null) attributes.put("preferred_username", data.get("username"));
        if (data.get("photo_url") != null) attributes.put("picture", data.get("photo_url"));

        if (linking) {
            IamPrincipal existing = (IamPrincipal) current.getPrincipal();
            provisioner.link(existing.userId(), IdentityProvider.TELEGRAM, id, attributes);
            return "redirect:/profile/accounts?linked";
        }

        var user = provisioner.provision(IdentityProvider.TELEGRAM, id, attributes);
        IamPrincipal principal = new IamPrincipal(loadUser.execute(user.username()), attributes);
        var authentication = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        new HttpSessionSecurityContextRepository().saveContext(context, request, response);
        return "redirect:/";
    }

    private String telegramHash(Map<String, String> data) {
        try {
            String dataCheckString = new TreeMap<>(data).entrySet().stream()
                    .filter(entry -> !"hash".equals(entry.getKey()) && !"email".equals(entry.getKey()))
                    .map(entry -> entry.getKey() + "=" + entry.getValue())
                    .reduce((left, right) -> left + "\n" + right)
                    .orElse("");
            byte[] secretKey = MessageDigest.getInstance("SHA-256")
                    .digest(botToken.getBytes(StandardCharsets.UTF_8));
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey, "HmacSHA256"));
            byte[] digest = mac.doFinal(dataCheckString.getBytes(StandardCharsets.UTF_8));
            return java.util.HexFormat.of().formatHex(digest);
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to verify Telegram authentication payload", exception);
        }
    }
}
