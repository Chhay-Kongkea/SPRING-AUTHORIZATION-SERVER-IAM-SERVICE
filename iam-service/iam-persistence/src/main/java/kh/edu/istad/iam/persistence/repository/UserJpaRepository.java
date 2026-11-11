
package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.UserJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUuid(UUID uuid);

    Optional<UserJpaEntity> findByUsername(String username);

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
