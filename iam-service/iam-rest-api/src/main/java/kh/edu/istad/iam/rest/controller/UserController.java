package kh.edu.istad.iam.rest.controller;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.command.CreateUserCommand;
import kh.edu.istad.iam.domain.command.AssignRolesToUserCommand;
import kh.edu.istad.iam.domain.command.UpdateUserProfileCommand;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.result.UserSummaryResult;
import kh.edu.istad.iam.domain.usecase.*;
import kh.edu.istad.iam.rest.dto.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final CreateUserUseCase createUser;
    private final AssignRolesToUserUseCase assignRoles;
    private final GetUserUseCase getUser;
    private final ListUsersUseCase listUsers;
    private final DeleteUserUseCase deleteUser;
    private final GetUserAuthoritiesUseCase getAuthorities;
    private final UpdateUserProfileUseCase updateProfile;

    public UserController(
            CreateUserUseCase createUser,
            AssignRolesToUserUseCase assignRoles,
            GetUserUseCase getUser,
            ListUsersUseCase listUsers,
            DeleteUserUseCase deleteUser,
            GetUserAuthoritiesUseCase getAuthorities,
            UpdateUserProfileUseCase updateProfile
    ) {
        this.createUser = createUser;
        this.assignRoles = assignRoles;
        this.getUser = getUser;
        this.listUsers = listUsers;
        this.deleteUser = deleteUser;
        this.getAuthorities = getAuthorities;
        this.updateProfile = updateProfile;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('user:create')")
    public Object create(@Valid @RequestBody CreateUserRequest request) {
        UserResult result = createUser.execute(new CreateUserCommand(
                request.username(),
                request.email(),
                request.password(),
                request.givenName(),
                request.familyName(),
                false,
                request.middleName(),
                request.nickname(),
                request.gender(),
                request.birthdate(),
                request.profileUrl(),
                request.pictureUrl(),
                request.websiteUrl(),
                request.zoneinfo(),
                request.locale(),
                request.phoneNumber(),
                request.address() == null ? null : request.address().streetAddress(),
                request.address() == null ? null : request.address().locality(),
                request.address() == null ? null : request.address().region(),
                request.address() == null ? null : request.address().postalCode(),
                request.address() == null ? null : request.address().country()
        ));

        if (request.roleUuids() != null && !request.roleUuids().isEmpty()) {
            assignRoles.execute(new AssignRolesToUserCommand(
                    result.id().value(),
                    request.roleUuids(),
                    null,
                    "Assigned during user creation"
            ));
        }

        return toResponse(getUser.execute(result.id().value()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public PageResult<UserSummaryResult> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "DESC") PageQuery.SortDirection direction
    ) {
        return listUsers.execute(new PageQuery(page, size, sort, direction));
    }

    public record ProfilePatch(
            String givenName,
            String familyName,
            String middleName,
            String nickname,
            String gender,
            java.time.LocalDate birthdate,
            String profileUrl,
            String pictureUrl,
            String websiteUrl,
            String zoneinfo,
            String locale,
            String phoneNumber,
            String streetAddress,
            String locality,
            String region,
            String postalCode,
            String country
    ) {
    }

    @PatchMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:update')")
    public Object update(
            @PathVariable UUID uuid,
            @RequestBody ProfilePatch request
    ) {
        return toResponse(updateProfile.execute(new UpdateUserProfileCommand(
                uuid,
                request.givenName(),
                request.familyName(),
                request.middleName(),
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
        )));
    }

    @GetMapping("/{uuid}")
    @PreAuthorize("hasAuthority('user:read')")
    public Map<String, Object> get(@PathVariable UUID uuid) {
        return toResponse(getUser.execute(uuid));
    }

    private Map<String, Object> toResponse(UserResult result) {
        var authorities = getAuthorities.execute(result.id().value());

        return Map.of(
                "uuid", result.id().value(),
                "username", result.username(),
                "email", result.email(),
                "emailVerified", result.emailVerified(),
                "name", result.givenName() + " " + result.familyName(),
                "locale", result.locale(),
                "enabled", result.enabled(),
                "roles", authorities.roles(),
                "createdAt", result.createdAt(),
                "updatedAt", result.updatedAt()
        );
    }

    @GetMapping("/{uuid}/authorities")
    @PreAuthorize("hasAuthority('user:read')")
    public Object authorities(@PathVariable UUID uuid) {
        return getAuthorities.execute(uuid);
    }

    @DeleteMapping("/{uuid}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('user:delete')")
    public void delete(@PathVariable UUID uuid) {
        deleteUser.execute(uuid);
    }
}