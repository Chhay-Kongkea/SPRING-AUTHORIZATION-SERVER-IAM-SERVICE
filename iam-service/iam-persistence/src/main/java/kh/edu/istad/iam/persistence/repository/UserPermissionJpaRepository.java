
package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.UserPermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPermissionJpaRepository
        extends JpaRepository<UserPermissionJpaEntity, Long> {

    List<UserPermissionJpaEntity> findAllByUser_IdOrderByAssignedAtAsc(Long userId);
}
