package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.valueobject.RoleId;

import java.util.UUID;

public class DeactivateRoleUseCase {

    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;

    public DeactivateRoleUseCase(
            RoleRepository roleRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.roleRepository = roleRepository;
        this.currentActorProvider = currentActorProvider;
    }

    public void execute(UUID roleUuid) {

        if (roleUuid == null) {
            throw new IllegalArgumentException("Role ID is required");
        }

        Role role = roleRepository.findById(new RoleId(roleUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("Role not found"));

        role.deactivate(currentActorProvider.getCurrentActor());

        roleRepository.save(role);
    }
}