
package kh.edu.istad.iam.persistence.adapter;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.domain.valueobject.UserId;
import kh.edu.istad.iam.persistence.entity.IdentityJpaEntity;
import kh.edu.istad.iam.persistence.entity.UserJpaEntity;
import kh.edu.istad.iam.persistence.mapper.IamPersistenceMapper;
import kh.edu.istad.iam.persistence.repository.IdentityJpaRepository;
import kh.edu.istad.iam.persistence.repository.UserJpaRepository;
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
public class IdentityRepositoryAdapter implements IdentityRepository {

    private final IdentityJpaRepository identityJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final IamPersistenceMapper mapper;

    public IdentityRepositoryAdapter(
            IdentityJpaRepository identityJpaRepository,
            UserJpaRepository userJpaRepository,
            IamPersistenceMapper mapper
    ) {
        this.identityJpaRepository = identityJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Identity save(Identity identity) {
        UserJpaEntity user = userJpaRepository
                .findByUuid(identity.getUserId().value())
                .orElseThrow(() -> new IllegalStateException(
                        "User not found: " + identity.getUserId().value()
                ));

        IdentityJpaEntity entity = identityJpaRepository
                .findByUuid(identity.getId().value())
                .orElse(null);

        if (entity == null) {
            entity = mapper.toJpaEntity(identity, user.getId());
        } else {
            mapper.updateJpaEntity(identity, user.getId(), entity);
        }

        IdentityJpaEntity saved = identityJpaRepository.save(entity);

        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Identity> findById(
            kh.edu.istad.iam.domain.valueobject.IdentityId identityId
    ) {
        return identityJpaRepository
                .findByUuid(identityId.value())
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Identity> findByProviderAndSubject(
            IdentityProvider provider,
            String providerSubject
    ) {
        return identityJpaRepository
                .findByProviderAndProviderSubject(provider, providerSubject)
                .map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Identity> findAllByUserId(UserId userId) {
        UserJpaEntity user = userJpaRepository
                .findByUuid(userId.value())
                .orElseThrow(() -> new IllegalStateException(
                        "User not found: " + userId.value()
                ));

        return identityJpaRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByProviderAndSubject(
            IdentityProvider provider,
            String providerSubject
    ) {
        return identityJpaRepository
                .existsByProviderAndProviderSubject(provider, providerSubject);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<Identity> findAll(PageQuery query) {
        Pageable pageable = createPageable(query);

        Page<IdentityJpaEntity> page =
                identityJpaRepository.findAll(pageable);

        List<Identity> content = page.getContent()
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

    @Override
    @Transactional(readOnly = true)
    public long countActiveByUserId(UserId userId) {
        UserJpaEntity user = userJpaRepository
                .findByUuid(userId.value())
                .orElseThrow(() -> new IllegalStateException(
                        "User not found: " + userId.value()
                ));

        return identityJpaRepository
                .countByUserIdAndDeletedFalse(user.getId());
    }

    private Identity toDomain(IdentityJpaEntity entity) {
        UserId userId = userJpaRepository
                .findById(entity.getUserId())
                .map(user -> new UserId(user.getUuid()))
                .orElseThrow(() -> new IllegalStateException(
                        "User not found for identity: " + entity.getUuid()
                ));

        return mapper.toDomain(entity, userId);
    }

    private Pageable createPageable(PageQuery query) {
        String sortBy = query.sortBy();

        List<String> allowedSortFields = List.of(
                "provider",
                "providerSubject",
                "email",
                "linkedAt",
                "lastLoginAt",
                "createdAt",
                "updatedAt"
        );

        if (!allowedSortFields.contains(sortBy)) {
            throw new IllegalArgumentException(
                    "Unsupported identity sort field: " + sortBy
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
