
package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.UserJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUuidAndDeletedFalse(UUID uuid);

    @Query("select u from UserJpaEntity u where u.uuid = :uuid")
    Optional<UserJpaEntity> findByUuidIncludingDeleted(@Param("uuid") UUID uuid);

    Optional<UserJpaEntity> findByUsernameAndDeletedFalse(String username);

    Optional<UserJpaEntity> findByEmailAndDeletedFalse(String email);

    boolean existsByUsernameAndDeletedFalse(String username);

    boolean existsByEmailAndDeletedFalse(String email);

    org.springframework.data.domain.Page<UserJpaEntity> findAllByDeletedFalse(
            org.springframework.data.domain.Pageable pageable);

}
