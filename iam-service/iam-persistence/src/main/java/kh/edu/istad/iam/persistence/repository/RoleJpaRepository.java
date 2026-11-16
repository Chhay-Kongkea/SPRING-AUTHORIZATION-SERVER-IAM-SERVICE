
package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.RoleJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {

    Optional<RoleJpaEntity> findByUuidAndDeletedFalse(UUID uuid);

    @Query("select r from RoleJpaEntity r where r.uuid = :uuid")
    Optional<RoleJpaEntity> findByUuidIncludingDeleted(@Param("uuid") UUID uuid);

    Optional<RoleJpaEntity> findByNameAndDeletedFalse(String name);

    boolean existsByNameAndDeletedFalse(String name);

    org.springframework.data.domain.Page<RoleJpaEntity> findAllByDeletedFalse(
            org.springframework.data.domain.Pageable pageable);

}
