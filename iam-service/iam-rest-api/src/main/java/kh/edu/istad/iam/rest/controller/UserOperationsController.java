package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.*;
import kh.edu.istad.iam.domain.usecase.*;
import kh.edu.istad.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.iam.rest.dto.AssignmentRequests;
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
    public void enable(@PathVariable UUID userUuid) {
        enable.execute(userUuid);
    }

    @PostMapping("/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void disable(@PathVariable UUID userUuid) {
        disable.execute(userUuid);
    }

    @PostMapping("/lock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void lock(@PathVariable UUID userUuid) {
        lock.execute(userUuid);
    }

    @PostMapping("/unlock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void unlock(@PathVariable UUID userUuid) {
        unlock.execute(userUuid);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void resetPassword(
            @PathVariable UUID userUuid,
            @RequestBody AssignmentRequests.PasswordReset request
    ) {
        resetPassword.execute(new ResetUserPasswordCommand(
                userUuid,
                request.newPassword()
        ));
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAuthority('user:assign')")
    public Object assignRoles(
            @PathVariable UUID userUuid,
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
            @PathVariable UUID userUuid,
            @PathVariable UUID roleUuid,
            @RequestParam(defaultValue = "Revoked by administrator") String reason
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
            @PathVariable UUID userUuid,
            @Valid @RequestBody AssignmentRequests.PermissionGrants request
    ) {
        PermissionEffect effect = PermissionEffect.valueOf(request.effect().toUpperCase());

        if (effect != PermissionEffect.GRANT && effect != PermissionEffect.DENY) {
            throw new IllegalArgumentException("Effect must be GRANT or DENY");
        }

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
            @PathVariable UUID userUuid,
            @PathVariable UUID permissionUuid,
            @RequestParam(defaultValue = "GRANT") PermissionEffect effect,
            @RequestParam(defaultValue = "Revoked by administrator") String reason
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
    public Object identities(@PathVariable UUID userUuid) {
        return identities.execute(userUuid);
    }

    @DeleteMapping("/identities/{identityUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:update')")
    public void unlink(
            @PathVariable UUID userUuid,
            @PathVariable UUID identityUuid
    ) {
        unlinkIdentity.execute(new UnlinkIdentityCommand(
                userUuid,
                identityUuid
        ));
    }
}