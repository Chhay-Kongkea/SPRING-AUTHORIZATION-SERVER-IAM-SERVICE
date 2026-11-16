package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.*;
import kh.edu.istad.iam.domain.usecase.*;
import kh.edu.istad.iam.rest.dto.AssignmentRequests;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles/{roleUuid}")
public class RoleOperationsController {

    private final UpdateRoleUseCase update;
    private final ActivateRoleUseCase activate;
    private final DeactivateRoleUseCase deactivate;
    private final DeleteRoleUseCase delete;
    private final AssignPermissionsToRoleUseCase assignPermissions;
    private final RevokePermissionFromRoleUseCase revokePermission;

    public RoleOperationsController(
            UpdateRoleUseCase update,
            ActivateRoleUseCase activate,
            DeactivateRoleUseCase deactivate,
            DeleteRoleUseCase delete,
            AssignPermissionsToRoleUseCase assignPermissions,
            RevokePermissionFromRoleUseCase revokePermission
    ) {
        this.update = update;
        this.activate = activate;
        this.deactivate = deactivate;
        this.delete = delete;
        this.assignPermissions = assignPermissions;
        this.revokePermission = revokePermission;
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('role:update')")
    public Object update(
            @PathVariable UUID roleUuid,
            @RequestBody MapRequest request
    ) {
        return update.execute(new UpdateRoleCommand(
                roleUuid,
                request.name(),
                request.description()
        ));
    }

    public record MapRequest(String name, String description) {
    }

    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:update')")
    public void activate(@PathVariable UUID roleUuid) {
        activate.execute(roleUuid);
    }

    @PostMapping("/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:update')")
    public void deactivate(@PathVariable UUID roleUuid) {
        deactivate.execute(roleUuid);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:delete')")
    public void delete(@PathVariable UUID roleUuid) {
        delete.execute(roleUuid);
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasAuthority('role:assign')")
    public Object assignPermissions(
            @PathVariable UUID roleUuid,
            @Valid @RequestBody AssignmentRequests.RolePermissions request
    ) {
        return assignPermissions.execute(new AssignPermissionsToRoleCommand(
                roleUuid,
                request.permissionUuids(),
                request.expiresAt(),
                request.reason()
        ));
    }

    @DeleteMapping("/permissions/{permissionUuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('role:assign')")
    public void revokePermission(
            @PathVariable UUID roleUuid,
            @PathVariable UUID permissionUuid,
            @RequestParam(defaultValue = "Revoked by administrator") String reason
    ) {
        revokePermission.execute(new RevokePermissionFromRoleCommand(
                roleUuid,
                permissionUuid,
                reason
        ));
    }
}