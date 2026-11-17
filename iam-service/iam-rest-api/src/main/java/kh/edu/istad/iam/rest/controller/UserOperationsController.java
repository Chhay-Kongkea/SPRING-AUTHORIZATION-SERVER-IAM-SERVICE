package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.*;
import kh.edu.istad.iam.domain.usecase.*;
import kh.edu.istad.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.iam.rest.dto.request.AssignmentRequests;
import kh.edu.istad.iam.rest.dto.request.ResetPasswordRequest;
import kh.edu.istad.iam.rest.dto.response.IdentityResponse;
import kh.edu.istad.iam.rest.support.ResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users/{userUuid}")
public class UserOperationsController {

    private final EnableUserUseCase enable;
    private final DisableUserUseCase disable;
    private final LockUserUseCase lock;
    private final UnlockUserUseCase unlock;
    private final ResetUserPasswordUseCase resetPassword;
    private final AssignRolesToUserUseCase assignRoles;
    private final RevokeRoleFromUserUseCase revokeRole;
    private final GrantPermissionsToUserUseCase grantPermissions;
    private final RevokePermissionFromUserUseCase revokePermission;
    private final GetUserIdentitiesUseCase identities;
    private final UnlinkIdentityUseCase unlinkIdentity;

    public UserOperationsController(
            EnableUserUseCase enable,
            DisableUserUseCase disable,
            LockUserUseCase lock,
            UnlockUserUseCase unlock,
            ResetUserPasswordUseCase resetPassword,
            AssignRolesToUserUseCase assignRoles,
            RevokeRoleFromUserUseCase revokeRole,
            GrantPermissionsToUserUseCase grantPermissions,
            RevokePermissionFromUserUseCase revokePermission,
            GetUserIdentitiesUseCase identities,
            UnlinkIdentityUseCase unlinkIdentity
    ) {
        this.enable = enable;
        this.disable = disable;
        this.lock = lock;
        this.unlock = unlock;
        this.resetPassword = resetPassword;
        this.assignRoles = assignRoles;
        this.revokeRole = revokeRole;
        this.grantPermissions = grantPermissions;
        this.revokePermission = revokePermission;
        this.identities = identities;
        this.unlinkIdentity = unlinkIdentity;
    }

    @PostMapping("/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void enable(@PathVariable("userUuid") UUID userUuid) {
        enable.execute(userUuid);
    }

    @PostMapping("/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void disable(@PathVariable("userUuid") UUID userUuid) {
        disable.execute(userUuid);
    }

    @PostMapping("/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void lock(@PathVariable("userUuid") UUID userUuid) {
        lock.execute(userUuid);
    }

    @PostMapping("/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void unlock(@PathVariable("userUuid") UUID userUuid) {

        unlock.execute(userUuid);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void resetPassword(
            @PathVariable("userUuid") UUID userUuid,
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        resetPassword.execute(new ResetUserPasswordCommand(
                userUuid,
                request.newPassword()
        ));
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('user:assign')")
    public Object assignRoles(
            @PathVariable("userUuid") UUID userUuid,
            @Valid @RequestBody AssignmentRequests.RoleAssignments request
    ) {
        return assignRoles.execute(new AssignRolesToUserCommand(
                userUuid,
                request.roleUuids(),
                request.expiresAt(),
                request.reason()
        ));
    }

    @DeleteMapping("/roles/{roleUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:assign')")
    public void revokeRole(
            @PathVariable("userUuid") UUID userUuid,
            @PathVariable("roleUuid") UUID roleUuid,
            @RequestParam(name = "reason",defaultValue = "Revoked by administrator") String reason
    ) {
        revokeRole.execute(new RevokeRoleFromUserCommand(
                userUuid,
                roleUuid,
                reason
        ));
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasAuthority('user:assign')")
    public Object grantPermissions(
            @PathVariable("userUuid") UUID userUuid,
            @Valid @RequestBody AssignmentRequests.PermissionGrants request
    ) {
        PermissionEffect effect = request.effect() == null ? PermissionEffect.GRANT : request.effect();

        return grantPermissions.execute(new GrantPermissionsToUserCommand(
                userUuid,
                request.permissionUuids(),
                effect,
                request.expiresAt(),
                request.reason()
        ));
    }

    @DeleteMapping("/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:assign')")
    public void revokePermission(
            @PathVariable("userUuid") UUID userUuid,
            @PathVariable("permissionUuid") UUID permissionUuid,
            @RequestParam(name = "effect",defaultValue = "GRANT") PermissionEffect effect,
            @RequestParam(name = "reason",defaultValue = "Revoked by administrator") String reason
    ) {
        revokePermission.execute(new RevokePermissionFromUserCommand(
                userUuid,
                permissionUuid,
                effect,
                reason
        ));
    }

    @GetMapping("/identities")
    @PreAuthorize("hasAuthority('user:read')")
    public java.util.List<IdentityResponse> identities(@PathVariable("permissionUuid") UUID userUuid) {
        return identities.execute(userUuid).stream().map(ResponseMapper::identity).toList();
    }

    @DeleteMapping("/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void unlink(
            @PathVariable("permissionUuid") UUID userUuid,
            @PathVariable("identityUuid") UUID identityUuid
    ) {
        unlinkIdentity.execute(new UnlinkIdentityCommand(
                userUuid,
                identityUuid
        ));
    }
}