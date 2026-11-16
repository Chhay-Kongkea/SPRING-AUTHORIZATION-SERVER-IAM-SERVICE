package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.AssignRolesToUserCommand;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.result.RoleAssignmentResult;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.valueobject.RoleId;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.UUID;

public class AssignRolesToUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    public AssignRolesToUserUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.roleRepository = Objects.requireNonNull(roleRepository);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public List<RoleAssignmentResult> execute(AssignRolesToUserCommand command) {

        Objects.requireNonNull(command, "Command cannot be null");
        Objects.requireNonNull(command.userId(), "User ID cannot be null");
        Objects.requireNonNull(command.roleIds(), "Role IDs cannot be null");

        if (command.roleIds().isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one role must be provided"
            );
        }

        Set<UUID> uniqueRoleIds = new HashSet<>(command.roleIds());

        if (uniqueRoleIds.size() != command.roleIds().size()) {
            throw new IllegalArgumentException(
                    "Duplicate role IDs are not allowed"
            );
        }

        User user = userRepository.findById(
                new UserId(command.userId())
        ).orElseThrow(() -> new NotFoundDomainException(
                "User not found: " + command.userId()
        ));

        String actor = currentActorProvider.getCurrentActor();

        for (UUID roleUuid : command.roleIds()) {

            Objects.requireNonNull(roleUuid, "Role ID cannot be null");

            Role role = roleRepository.findById(
                    new RoleId(roleUuid)
            ).orElseThrow(() -> new NotFoundDomainException(
                    "Role not found: " + roleUuid
            ));

            if (!role.isActive()) {
                throw new IllegalStateException(
                        "Cannot assign inactive role: " + role.getName()
                );
            }
        }

        for (UUID roleUuid : command.roleIds()) {
            user.assignRole(new RoleId(roleUuid), actor, command.expiresAt(), command.reason());
        }

        User saved = userRepository.save(user);
        return saved.getRoles().stream()
                .filter(UserRole::isActive)
                .filter(assignment -> command.roleIds().contains(assignment.getRoleId().value()))
                .map(assignment -> {
                    Role role = roleRepository.findById(assignment.getRoleId())
                            .orElseThrow(() -> new NotFoundDomainException("Role not found"));
                    return new RoleAssignmentResult(assignment.getRoleId().value(), role.getName(),
                            assignment.getAssignedAt(), assignment.getExpiresAt(),
                            assignment.getAssignedBy(), assignment.getReason());
                }).toList();
    }
}