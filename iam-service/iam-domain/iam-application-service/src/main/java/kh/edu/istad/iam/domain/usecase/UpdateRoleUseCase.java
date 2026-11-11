package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.UpdateRoleCommand;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.result.RoleResult;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.util.Set;

public class UpdateRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamMapper iamMapper;

    public UpdateRoleUseCase(
            RoleRepository roleRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.roleRepository = roleRepository;
        this.currentActorProvider = currentActorProvider;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public RoleResult execute(UpdateRoleCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("Update role command is required");
        }

        if (command.roleId() == null) {
            throw new IllegalArgumentException("Role ID is required");
        }

        if (command.name() == null || command.name().isBlank()) {
            throw new IllegalArgumentException("Role name is required");
        }

        RoleId roleId = new RoleId(command.roleId());
        String name = command.name().trim().toUpperCase();

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Role not found"));

        if (!role.getName().equals(name)
                && roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("Role name already exists");
        }

        String actor = currentActorProvider.getCurrentActor();

        role.updateDetails(name, command.description(), actor);

        Role savedRole = roleRepository.save(role);

        return iamMapper.toRoleResult(savedRole, Set.of());
    }
}