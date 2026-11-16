package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.CreatePermissionCommand;
import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.result.PermissionResult;
import kh.edu.istad.iam.domain.usecase.CreatePermissionUseCase;
import kh.edu.istad.iam.domain.usecase.GetPermissionUseCase;
import kh.edu.istad.iam.domain.usecase.ListPermissionsUseCase;
import kh.edu.istad.iam.rest.dto.CreatePermissionRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final CreatePermissionUseCase createPermission;
    private final GetPermissionUseCase getPermission;
    private final ListPermissionsUseCase listPermissions;

    public PermissionController(
            CreatePermissionUseCase createPermission,
            GetPermissionUseCase getPermission,
            ListPermissionsUseCase listPermissions
    ) {
        this.createPermission = createPermission;
        this.getPermission = getPermission;
        this.listPermissions = listPermissions;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('permission:create')")
    public PermissionResult create(@Valid @RequestBody CreatePermissionRequest request) {
        return createPermission.execute(new CreatePermissionCommand(
                request.resource(),
                request.action(),
                request.description()
        ));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('permission:read')")
    public PermissionResult get(@PathVariable UUID uuid) {
        return getPermission.execute(uuid);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('permission:read')")
    public PageResult<PermissionResult> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") PageQuery.SortDirection direction
    ) {
        return listPermissions.execute(new PageQuery(page, size, sort, direction));
    }
}