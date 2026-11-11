
package kh.edu.istad.iam.persistence.adapter;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.entity.RolePermission;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.persistence.entity.PermissionJpaEntity;
import kh.edu.istad.iam.persistence.entity.RoleJpaEntity;
import kh.edu.istad.iam.persistence.entity.RolePermissionJpaEntity;
import kh.edu.istad.iam.persistence.mapper.IamPersistenceMapper;
import kh.edu.istad.iam.persistence.repository.PermissionJpaRepository;
import kh.edu.istad.iam.persistence.repository.RoleJpaRepository;
import kh.edu.istad.iam.persistence.repository.RolePermissionJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Transactional
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final IamPersistenceMapper mapper;

    public RoleRepositoryAdapter(
            RoleJpaRepository roleJpaRepository,
            PermissionJpaRepository permissionJpaRepository,
            RolePermissionJpaRepository rolePermissionJpaRepository,
            IamPersistenceMapper mapper
    ) {
        this.roleJpaRepository = roleJpaRepository;
        this.permissionJpaRepository = permissionJpaRepository;
        this.rolePermissionJpaRepository = rolePermissionJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Role save(Role role) {
        RoleJpaEntity entity = roleJpaRepository
                .findByUuid(role.getId().value())
                .orElseGet(() -> mapper.toJpaEntity(role));

        if (entity.getId() != null) {
            mapper.updateJpaEntity(role, entity);
        }

        RoleJpaEntity savedRole = roleJpaRepository.save(entity);

        saveRolePermissions(role, savedRole);

        return findById(role.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Saved role could not be reloaded"
                ));
    }

    private void saveRolePermissions(
            Role role,
            RoleJpaEntity savedRole
    ) {
        List<RolePermissionJpaEntity> existingAssignments =
                rolePermissionJpaRepository
                        .findAllByRole_IdOrderByAssignedAtAsc(savedRole.getId());

        for (RolePermission rolePermission : role.getPermissions()) {

            RolePermissionJpaEntity existing = findExistingAssignment(
                    rolePermission,
                    existingAssignments
            );

            if (rolePermission.getId() != null && existing == null) {
                throw new IllegalStateException(
                        "Role permission assignment does not belong to this role"
                );
            }

            PermissionJpaEntity permission = permissionJpaRepository
                    .findByUuid(rolePermission.getPermissionId().value())
                    .orElseThrow(() -> new IllegalStateException(
                            "Permission not found: "
                                    + rolePermission.getPermissionId().value()
                    ));

            RolePermissionJpaEntity assignment =
                    mapper.toJpaEntity(
                            rolePermission,
                            savedRole,
                            permission
                    );

            if (existing != null) {
                assignment.setId(existing.getId());
            }

            rolePermissionJpaRepository.save(assignment);
        }
    }

    private RolePermissionJpaEntity findExistingAssignment(
            RolePermission rolePermission,
            List<RolePermissionJpaEntity> existingAssignments
    ) {
        if (rolePermission.getId() != null) {
            return existingAssignments.stream()
                    .filter(existing ->
                            existing.getId().equals(rolePermission.getId())
                    )
                    .findFirst()
                    .orElse(null);
        }

        return existingAssignments.stream()
                .filter(existing ->
                        existing.getPermission().getUuid().equals(
                                rolePermission.getPermissionId().value()
                        )
                        && existing.getAssignedAt().equals(
                                rolePermission.getAssignedAt()
                        )
                        && existing.getAssignedBy().equals(
                                rolePermission.getAssignedBy()
                        )
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(
            kh.edu.istad.iam.domain.valueobject.RoleId roleId
    ) {
        return roleJpaRepository
                .findByUuid(roleId.value())
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findByName(String name) {
        return roleJpaRepository
                .findByName(name)
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Role> findAll(PageQuery query) {
        Pageable pageable = createPageable(query);

        Page<RoleJpaEntity> page = roleJpaRepository.findAll(pageable);

        List<Role> content = page.getContent()
                .stream()
                .map(this::toDomain)
                .toList();

        return new PageResult<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }

    private Role toDomain(RoleJpaEntity entity) {
        List<RolePermission> permissions =
                rolePermissionJpaRepository
                        .findAllByRole_IdOrderByAssignedAtAsc(entity.getId())
                        .stream()
                        .map(mapper::toDomain)
                        .toList();

        return mapper.toDomain(entity, permissions);
    }

    private Pageable createPageable(PageQuery query) {
        String sortBy = query.sortBy();

        List<String> allowedSortFields = List.of(
                "name",
                "createdAt",
                "updatedAt"
        );

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Unsupported role sort field: " + sortBy
            );
        }

        Sort.Direction direction =
                query.direction() == PageQuery.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        return PageRequest.of(
                query.page(),
                query.size(),
                Sort.by(direction, sortBy)
        );
    }
}
