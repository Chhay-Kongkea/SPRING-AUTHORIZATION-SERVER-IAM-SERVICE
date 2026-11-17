package kh.edu.istad.iam.rest.controller;

import jakarta.validation.Valid;
import kh.edu.istad.iam.domain.command.ChangePasswordCommand;
import kh.edu.istad.iam.domain.command.UnlinkIdentityCommand;
import kh.edu.istad.iam.domain.usecase.ChangePasswordUseCase;
import kh.edu.istad.iam.domain.usecase.GetUserIdentitiesUseCase;
import kh.edu.istad.iam.domain.usecase.GetUserUseCase;
import kh.edu.istad.iam.domain.usecase.UnlinkIdentityUseCase;
import kh.edu.istad.iam.domain.usecase.UpdateUserProfileUseCase;
import kh.edu.istad.iam.rest.dto.request.ChangePasswordRequest;
import kh.edu.istad.iam.rest.dto.request.UpdateUserProfileRequest;
import kh.edu.istad.iam.rest.dto.response.IdentityResponse;
import kh.edu.istad.iam.rest.dto.response.UserResponse;
import kh.edu.istad.iam.rest.support.CurrentUser;
import kh.edu.istad.iam.rest.support.ResponseMapper;
import kh.edu.istad.iam.rest.support.UserResponseAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/me")
public class SelfServiceController {

    private final GetUserUseCase getUser;
    private final UpdateUserProfileUseCase updateProfile;
    private final ChangePasswordUseCase changePassword;
    private final GetUserIdentitiesUseCase identities;
    private final UnlinkIdentityUseCase unlinkIdentity;
    private final UserResponseAssembler assembler;

    public SelfServiceController(GetUserUseCase getUser, UpdateUserProfileUseCase updateProfile,
                                 ChangePasswordUseCase changePassword, GetUserIdentitiesUseCase identities,
                                 UnlinkIdentityUseCase unlinkIdentity, UserResponseAssembler assembler) {
        this.getUser = getUser;
        this.updateProfile = updateProfile;
        this.changePassword = changePassword;
        this.identities = identities;
        this.unlinkIdentity = unlinkIdentity;
        this.assembler = assembler;
    }

    @GetMapping
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return assembler.assemble(getUser.execute(CurrentUser.id(jwt)));
    }

    @PatchMapping
    public UserResponse update(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody UpdateUserProfileRequest request) {
        return assembler.assemble(updateProfile.execute(UserController.toCommand(CurrentUser.id(jwt), request)));
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody ChangePasswordRequest request) {
        if (!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Password confirmation does not match");
        }
        changePassword.execute(new ChangePasswordCommand(
                CurrentUser.id(jwt), request.currentPassword(), request.newPassword()));
    }

    @GetMapping("/identities")
    public List<IdentityResponse> identities(@AuthenticationPrincipal Jwt jwt) {
        return identities.execute(CurrentUser.id(jwt)).stream().map(ResponseMapper::identity).toList();
    }

    @DeleteMapping("/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlink(@AuthenticationPrincipal Jwt jwt,
                       @PathVariable("identityUuid") UUID identityUuid) {
        unlinkIdentity.execute(new UnlinkIdentityCommand(CurrentUser.id(jwt), identityUuid));
    }
}
