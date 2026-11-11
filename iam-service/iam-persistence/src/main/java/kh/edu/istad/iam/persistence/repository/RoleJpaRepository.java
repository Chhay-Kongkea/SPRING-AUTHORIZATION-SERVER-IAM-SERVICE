
package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.RoleJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {

    Optional<RoleJpaEntity> findByUuid(UUID uuid);

    Optional<RoleJpaEntity> findByName(String name);

    boolean existsByName(String name);

}
