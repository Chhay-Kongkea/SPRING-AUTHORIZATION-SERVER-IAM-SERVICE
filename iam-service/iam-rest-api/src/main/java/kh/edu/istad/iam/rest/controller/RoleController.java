package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.CreateRoleCommand;
import kh.edu.istad.iam.domain.command.AssignPermissionsToRoleCommand;
import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.result.RoleResult;
import kh.edu.istad.iam.domain.result.RoleSummaryResult;
import kh.edu.istad.iam.domain.usecase.CreateRoleUseCase;
import kh.edu.istad.iam.domain.usecase.AssignPermissionsToRoleUseCase;
import kh.edu.istad.iam.domain.usecase.GetRoleUseCase;
import kh.edu.istad.iam.domain.usecase.ListRolesUseCase;
import kh.edu.istad.iam.rest.dto.CreateRoleRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final CreateRoleUseCase createRole;
    private final AssignPermissionsToRoleUseCase assignPermissions;
    private final GetRoleUseCase getRole;
    private final ListRolesUseCase listRoles;

    public RoleController(
            CreateRoleUseCase createRole,
            AssignPermissionsToRoleUseCase assignPermissions,
            GetRoleUseCase getRole,
            ListRolesUseCase listRoles
    ) {
        this.createRole = createRole;
        this.assignPermissions = assignPermissions;
        this.getRole = getRole;
        this.listRoles = listRoles;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('role:create')")
    public RoleResult create(@Valid @RequestBody CreateRoleRequest request) {
        RoleResult role = createRole.execute(new CreateRoleCommand(
                request.name(),
                request.description()
        ));

        if (request.permissionUuids() != null && !request.permissionUuids().isEmpty()) {
            assignPermissions.execute(new AssignPermissionsToRoleCommand(
                    role.id().value(),
                    request.permissionUuids(),
                    null,
                    "Assigned during role creation"
            ));

            return getRole.execute(role.id().value());
        }

        return role;
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('role:read')")
    public RoleResult get(@PathVariable UUID uuid) {
        return getRole.execute(uuid);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('role:read')")
    public PageResult<RoleSummaryResult> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") PageQuery.SortDirection direction
    ) {
        return listRoles.execute(new PageQuery(page, size, sort, direction));
    }
}