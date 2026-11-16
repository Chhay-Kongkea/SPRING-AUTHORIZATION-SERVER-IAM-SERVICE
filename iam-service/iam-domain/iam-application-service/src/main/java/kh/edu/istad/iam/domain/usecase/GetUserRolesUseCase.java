package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.UserRoleResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GetUserRolesUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public GetUserRolesUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.roleRepository = Objects.requireNonNull(roleRepository);
    }

    public List<UserRoleResult> execute(UUID userUuid) {

        Objects.requireNonNull(userUuid, "User ID cannot be null");

        User user = userRepository.findById(
                new UserId(userUuid)
        ).orElseThrow(() -> new NotFoundDomainException(
                "User not found: " + userUuid
        ));

        Instant now = Instant.now();

        return user.getRoles().stream()
                .map(userRole -> mapToResult(userRole, now))
                .toList();
    }

    private UserRoleResult mapToResult(UserRole userRole, Instant now) {

        Role role = roleRepository.findById(
                userRole.getRoleId()
        ).orElseThrow(() -> new NotFoundDomainException(
                "Role not found: " + userRole.getRoleId()
        ));

        boolean active = userRole.isActive()
                && (userRole.getExpiresAt() == null
                || userRole.getExpiresAt().isAfter(now))
                && role.isActive();

        return new UserRoleResult(
                role.getId().value(),
                role.getName(),
                userRole.getAssignedAt(),
                userRole.getAssignedBy(),
                userRole.getExpiresAt(),
                userRole.getRevokedAt(),
                userRole.getRevokedBy(),
                userRole.getReason(),
                active
        );
    }
}