package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.persistence.entity.IdentityJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentityJpaRepository
        extends JpaRepository<IdentityJpaEntity, Long> {

    Optional<IdentityJpaEntity> findByUuidAndDeletedFalse(UUID uuid);

    @Query("select i from IdentityJpaEntity i where i.uuid = :uuid")
    Optional<IdentityJpaEntity> findByUuidIncludingDeleted(@Param("uuid") UUID uuid);

    Optional<IdentityJpaEntity> findByProviderAndProviderSubjectAndDeletedFalse(
            IdentityProvider provider,
            String providerSubject
    );

    boolean existsByProviderAndProviderSubjectAndDeletedFalse(
            IdentityProvider provider,
            String providerSubject
    );

    List<IdentityJpaEntity> findAllByUserIdAndDeletedFalse(Long userId);

    org.springframework.data.domain.Page<IdentityJpaEntity> findAllByDeletedFalse(
            org.springframework.data.domain.Pageable pageable);

    long countByUserIdAndDeletedFalse(Long userId);
}
