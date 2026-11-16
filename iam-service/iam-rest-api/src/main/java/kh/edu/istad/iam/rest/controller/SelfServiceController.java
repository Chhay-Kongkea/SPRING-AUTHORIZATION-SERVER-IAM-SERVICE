package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.ChangePasswordCommand;
import kh.edu.istad.iam.domain.command.UnlinkIdentityCommand;
import kh.edu.istad.iam.domain.command.UpdateUserProfileCommand;
import kh.edu.istad.iam.domain.usecase.*;
import kh.edu.istad.iam.rest.dto.AssignmentRequests;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
@RequestMapping("/api/me")
public class SelfServiceController {

    private final GetUserUseCase getUser;
    private final UpdateUserProfileUseCase updateProfile;
    private final ChangePasswordUseCase changePassword;
    private final GetUserIdentitiesUseCase identities;
    private final UnlinkIdentityUseCase unlinkIdentity;

    public SelfServiceController(
            GetUserUseCase getUser,
            UpdateUserProfileUseCase updateProfile,
            ChangePasswordUseCase changePassword,
            GetUserIdentitiesUseCase identities,
            UnlinkIdentityUseCase unlinkIdentity
    ) {
        this.getUser = getUser;
        this.updateProfile = updateProfile;
        this.changePassword = changePassword;
        this.identities = identities;
        this.unlinkIdentity = unlinkIdentity;
    }

    @GetMapping
    public Object me(@AuthenticationPrincipal Jwt jwt) {
        return getUser.execute(UUID.fromString(jwt.getSubject()));
    }

    @PatchMapping
    public Object update(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody AssignmentRequests.ProfileUpdate request
    ) {
        UUID id = UUID.fromString(jwt.getSubject());

        return updateProfile.execute(new UpdateUserProfileCommand(
                id,
                request.givenName(),
                request.familyName(),
                null,
                request.nickname(),
                request.gender(),
                request.birthdate(),
                request.profileUrl(),
                request.pictureUrl(),
                request.websiteUrl(),
                request.zoneinfo(),
                request.locale(),
                request.phoneNumber(),
                request.streetAddress(),
                request.locality(),
                request.region(),
                request.postalCode(),
                request.country()
        ));
    }

    public record PasswordChange(
            String currentPassword,
            String newPassword,
            String confirmPassword
    ) {
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody PasswordChange request
    ) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Password confirmation does not match");
        }

        changePassword.execute(new ChangePasswordCommand(
                UUID.fromString(jwt.getSubject()),
                request.currentPassword(),
                request.newPassword()
        ));
    }

    @GetMapping("/identities")
    public Object identities(@AuthenticationPrincipal Jwt jwt) {
        return identities.execute(UUID.fromString(jwt.getSubject()));
    }

    @DeleteMapping("/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlink(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID identityUuid
    ) {
        unlinkIdentity.execute(new UnlinkIdentityCommand(
                UUID.fromString(jwt.getSubject()),
                identityUuid
        ));
    }
}