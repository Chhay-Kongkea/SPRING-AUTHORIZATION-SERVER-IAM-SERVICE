
package kh.edu.istad.iam.persistence.adapter;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Permission;
import kh.edu.istad.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.persistence.entity.PermissionJpaEntity;
import kh.edu.istad.iam.persistence.mapper.IamPersistenceMapper;
import kh.edu.istad.iam.persistence.repository.PermissionJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final IamPersistenceMapper mapper;

    public PermissionRepositoryAdapter(
            PermissionJpaRepository permissionJpaRepository,
            IamPersistenceMapper mapper
    ) {
        this.permissionJpaRepository = permissionJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Permission save(Permission permission) {
        PermissionJpaEntity entity = permissionJpaRepository
                .findByUuid(permission.getId().value())
                .orElseGet(() -> mapper.toJpaEntity(permission));

        if (entity.getId() != null) {
            mapper.updateJpaEntity(permission, entity);
        }

        PermissionJpaEntity savedEntity =
                permissionJpaRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> findById(
            kh.edu.istad.iam.domain.valueobject.PermissionId permissionId
    ) {
        return permissionJpaRepository
                .findByUuid(permissionId.value())
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Permission> findByName(String name) {
        return permissionJpaRepository
                .findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return permissionJpaRepository.existsByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Permission> findAll(PageQuery query) {
        Pageable pageable = createPageable(query);

        Page<PermissionJpaEntity> page =
                permissionJpaRepository.findAll(pageable);

        List<Permission> content = page.getContent()
                .stream()
                .map(mapper::toDomain)
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

    private Pageable createPageable(PageQuery query) {
        String sortBy = query.sortBy();

        List<String> allowedSortFields = List.of(
                "name",
                "resource",
                "action",
                "createdAt",
                "updatedAt"
        );

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Unsupported permission sort field: " + sortBy
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
