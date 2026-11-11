package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.CreateRoleCommand;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.result.RoleResult;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.util.Set;

public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamMapper iamMapper;

    public CreateRoleUseCase(
            RoleRepository roleRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.roleRepository = roleRepository;
        this.currentActorProvider = currentActorProvider;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public RoleResult execute(CreateRoleCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("Create role command is required");
        }

        if (command.name() == null || command.name().isBlank()) {
            throw new IllegalArgumentException("Role name is required");
        }

        String name = command.name().trim().toUpperCase();

        if (roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        String actor = currentActorProvider.getCurrentActor();

        Role role = new Role(
                RoleId.generate(),
                name,
                command.description(),
                false,
                actor
        );

        Role savedRole = roleRepository.save(role);

        return iamMapper.toRoleResult(savedRole, Set.of());
    }
}