package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.CreateRoleCommand;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.exception.ConflictDomainException;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.result.RoleResult;
import kh.edu.istad.iam.domain.valueobject.PermissionId;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamMapper iamMapper;

    public CreateRoleUseCase(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.roleRepository = Objects.requireNonNull(roleRepository);
        this.permissionRepository = Objects.requireNonNull(permissionRepository);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
        this.iamMapper = IamMapper.INSTANCE;
    }

    public RoleResult execute(CreateRoleCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Create role command is required");
        }
        if (command.name() == null || command.name().isBlank()) {
            throw new IllegalArgumentException("Role name is required");
        }

        String name = command.name().trim().toUpperCase(Locale.ROOT);
        if (roleRepository.existsByName(name)) {
            throw new ConflictDomainException("Role name already exists: " + name);
        }

        String actor = currentActorProvider.getCurrentActor();
        Role role = new Role(RoleId.generate(), name, command.description(), false, actor);

        Set<String> permissionNames = new TreeSet<>();
        if (command.permissionIds() != null) {
            for (UUID permissionUuid : new LinkedHashSet<>(command.permissionIds())) {
                PermissionId permissionId = new PermissionId(permissionUuid);
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new NotFoundDomainException(
                                "Permission not found: " + permissionUuid));
                if (!permission.isActive()) {
                    throw new IllegalStateException(
                            "Cannot assign inactive permission: " + permission.getName());
                }
                role.assignPermission(permissionId, actor, null, "Assigned during role creation");
                permissionNames.add(permission.getName());
            }
        }

        Role saved = roleRepository.save(role);
        return iamMapper.toRoleResult(saved, permissionNames);
    }
}
