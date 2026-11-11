package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.UpdatePermissionCommand;
import kh.edu.istad.iam.domain.usecase.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions/{permissionUuid}")
public class PermissionOperationsController {

    private final UpdatePermissionUseCase update;
    private final ActivatePermissionUseCase activate;
    private final DeactivatePermissionUseCase deactivate;
    private final DeletePermissionUseCase delete;

    public PermissionOperationsController(
            UpdatePermissionUseCase update,
            ActivatePermissionUseCase activate,
            DeactivatePermissionUseCase deactivate,
            DeletePermissionUseCase delete
    ) {
        this.update = update;
        this.activate = activate;
        this.deactivate = deactivate;
        this.delete = delete;
    }

    public record UpdateRequest(
            String resource,
            String action,
            String description
    ) {
    }

    @PatchMapping
    @PreAuthorize("hasAuthority('permission:update')")
    public Object update(
            @PathVariable UUID permissionUuid,
            @RequestBody UpdateRequest request
    ) {
        return update.execute(new UpdatePermissionCommand(
                permissionUuid,
                request.resource(),
                request.action(),
                request.description()
        ));
    }

    @PostMapping("/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:update')")
    public void activate(@PathVariable UUID permissionUuid) {
        activate.execute(permissionUuid);
    }

    @PostMapping("/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:update')")
    public void deactivate(@PathVariable UUID permissionUuid) {
        deactivate.execute(permissionUuid);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('permission:delete')")
    public void delete(@PathVariable UUID permissionUuid) {
        delete.execute(permissionUuid);
    }
}