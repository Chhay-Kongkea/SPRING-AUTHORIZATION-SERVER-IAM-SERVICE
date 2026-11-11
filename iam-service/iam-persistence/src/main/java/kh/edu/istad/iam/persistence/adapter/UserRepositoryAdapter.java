
package kh.edu.istad.iam.persistence.adapter;


import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.entity.UserPermission;
import kh.edu.istad.iam.domain.entity.UserRole;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.result.UserSummaryResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import kh.edu.istad.iam.persistence.entity.UserJpaEntity;
import kh.edu.istad.iam.persistence.entity.UserPermissionJpaEntity;
import kh.edu.istad.iam.persistence.entity.UserRoleJpaEntity;
import kh.edu.istad.iam.persistence.mapper.IamPersistenceMapper;
import kh.edu.istad.iam.persistence.repository.PermissionJpaRepository;
import kh.edu.istad.iam.persistence.repository.RoleJpaRepository;
import kh.edu.istad.iam.persistence.repository.UserJpaRepository;
import kh.edu.istad.iam.persistence.repository.UserPermissionJpaRepository;
import kh.edu.istad.iam.persistence.repository.UserRoleJpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public class UserRepositoryAdapter implements UserRepository {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "username",
            "email",
            "createdAt",
            "updatedAt",
            "lastLoginAt"
    );

    private final UserJpaRepository userJpaRepository;
    private final RoleJpaRepository roleJpaRepository;
    private final PermissionJpaRepository permissionJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserPermissionJpaRepository userPermissionJpaRepository;
    private final IamPersistenceMapper mapper;

    public UserRepositoryAdapter(
            UserJpaRepository userJpaRepository,
            RoleJpaRepository roleJpaRepository,
            PermissionJpaRepository permissionJpaRepository,
            UserRoleJpaRepository userRoleJpaRepository,
            UserPermissionJpaRepository userPermissionJpaRepository,
            IamPersistenceMapper mapper
    ) {
        this.userJpaRepository = userJpaRepository;
        this.roleJpaRepository = roleJpaRepository;
        this.permissionJpaRepository = permissionJpaRepository;
        this.userRoleJpaRepository = userRoleJpaRepository;
        this.userPermissionJpaRepository = userPermissionJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {

        UserJpaEntity userEntity = userJpaRepository
                .findByUuid(user.getId().value())
                .orElseGet(() -> mapper.toJpaEntity(user));

        if (userEntity.getId() != null) {
            mapper.updateJpaEntity(user, userEntity);
        }

        UserJpaEntity savedUser = userJpaRepository.save(userEntity);

        saveUserRoles(user, savedUser);
        saveUserPermissions(user, savedUser);

        return findById(user.getId())
                .orElseThrow(() -> new IllegalStateException(
                        "Could not reload saved user: " + user.getId().value()
                ));
    }

    private void saveUserRoles(
            User user,
            UserJpaEntity userEntity
    ) {

        List<UserRoleJpaEntity> existingAssignments =
                userRoleJpaRepository.findAllByUser_IdOrderByAssignedAtAsc(
                        userEntity.getId()
                );

        for (UserRole userRole : user.getRoles()) {

            UserRoleJpaEntity existing = findExistingUserRole(
                    userRole,
                    existingAssignments
            );

            var roleEntity = roleJpaRepository
                    .findByUuid(userRole.getRoleId().value())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Role not found: " + userRole.getRoleId().value()
                    ));

            UserRoleJpaEntity assignmentEntity = mapper.toJpaEntity(
                    userRole,
                    userEntity,
                    roleEntity
            );

            // Preserve the existing database ID when this is an update.
            if (existing != null) {
                assignmentEntity.setId(existing.getId());
            }

            userRoleJpaRepository.save(assignmentEntity);
        }
    }

    private UserRoleJpaEntity findExistingUserRole(
            UserRole userRole,
            List<UserRoleJpaEntity> existingAssignments
    ) {


        if (userRole.getId() != null) {
            Optional<UserRoleJpaEntity> byId = existingAssignments.stream()
                    .filter(entity -> userRole.getId().equals(entity.getId()))
                    .findFirst();

            if (byId.isPresent()) {
                return byId.get();
            }
        }

        return existingAssignments.stream()
                .filter(entity ->
                        entity.getRole().getUuid().equals(
                                userRole.getRoleId().value()
                        )
                                && entity.getAssignedAt().equals(
                                userRole.getAssignedAt()
                        )
                                && entity.getAssignedBy().equals(
                                userRole.getAssignedBy()
                        )
                )
                .findFirst()
                .orElse(null);
    }

    private void saveUserPermissions(
            User user,
            UserJpaEntity userEntity
    ) {

        List<UserPermissionJpaEntity> existingAssignments =
                userPermissionJpaRepository
                        .findAllByUser_IdOrderByAssignedAtAsc(
                                userEntity.getId()
                        );

        for (UserPermission userPermission : user.getPermissions()) {

            UserPermissionJpaEntity existing = findExistingUserPermission(
                    userPermission,
                    existingAssignments
            );

            var permissionEntity = permissionJpaRepository
                    .findByUuid(userPermission.getPermissionId().value())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Permission not found: "
                                    + userPermission.getPermissionId().value()
                    ));

            UserPermissionJpaEntity assignmentEntity = mapper.toJpaEntity(
                    userPermission,
                    userEntity,
                    permissionEntity
            );

            // Preserve the existing database ID when this is an update.
            if (existing != null) {
                assignmentEntity.setId(existing.getId());
            }

            userPermissionJpaRepository.save(assignmentEntity);
        }
    }

    private UserPermissionJpaEntity findExistingUserPermission(
            UserPermission userPermission,
            List<UserPermissionJpaEntity> existingAssignments
    ) {

        if (userPermission.getId() != null) {
            Optional<UserPermissionJpaEntity> byId =
                    existingAssignments.stream()
                            .filter(entity ->
                                    userPermission.getId().equals(entity.getId())
                            )
                            .findFirst();

            if (byId.isPresent()) {
                return byId.get();
            }
        }

        return existingAssignments.stream()
                .filter(entity ->
                        entity.getPermission().getUuid().equals(
                                userPermission.getPermissionId().value()
                        )
                                && entity.getEffect() == userPermission.getEffect()
                                && entity.getAssignedAt().equals(
                                userPermission.getAssignedAt()
                        )
                                && entity.getAssignedBy().equals(
                                userPermission.getAssignedBy()
                        )
                )
                .findFirst()
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UserId userId) {

        return userJpaRepository.findByUuid(userId.value())
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {

        return userJpaRepository.findByUsername(username)
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {

        return userJpaRepository.findByEmail(email)
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {

        return userJpaRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {

        return userJpaRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<UserSummaryResult> findAll(PageQuery query) {

        String sortBy = ALLOWED_SORT_FIELDS.contains(query.sortBy())
                ? query.sortBy()
                : "createdAt";

        Sort.Direction direction =
                query.direction() == PageQuery.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC;

        PageRequest pageable = PageRequest.of(
                query.page(),
                query.size(),
                Sort.by(direction, sortBy)
        );

        Page<UserJpaEntity> page = userJpaRepository.findAll(pageable);

        List<UserSummaryResult> content = page.getContent()
                .stream()
                .map(this::toDomain)
                .map(IamMapper.INSTANCE::toUserSummaryResult)
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

    private User toDomain(UserJpaEntity entity) {

        List<UserRole> roles = userRoleJpaRepository
                .findAllByUser_IdOrderByAssignedAtAsc(entity.getId())
                .stream()
                .map(mapper::toDomain)
                .toList();

        List<UserPermission> permissions = userPermissionJpaRepository
                .findAllByUser_IdOrderByAssignedAtAsc(entity.getId())
                .stream()
                .map(mapper::toDomain)
                .toList();

        return mapper.toDomain(
                entity,
                roles,
                permissions
        );
    }
}
