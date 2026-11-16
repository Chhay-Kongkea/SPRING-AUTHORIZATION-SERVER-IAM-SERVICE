
package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.PermissionJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PermissionJpaRepository
        extends JpaRepository<PermissionJpaEntity, Long> {

    Optional<PermissionJpaEntity> findByUuidAndDeletedFalse(UUID uuid);

    @Query("select p from PermissionJpaEntity p where p.uuid = :uuid")
    Optional<PermissionJpaEntity> findByUuidIncludingDeleted(@Param("uuid") UUID uuid);

    Optional<PermissionJpaEntity> findByNameAndDeletedFalse(String name);

    boolean existsByNameAndDeletedFalse(String name);

    org.springframework.data.domain.Page<PermissionJpaEntity> findAllByDeletedFalse(
            org.springframework.data.domain.Pageable pageable);

}
