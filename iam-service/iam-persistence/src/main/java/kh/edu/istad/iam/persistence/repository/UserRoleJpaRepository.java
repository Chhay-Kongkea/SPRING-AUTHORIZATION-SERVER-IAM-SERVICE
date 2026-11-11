
package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.UserRoleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleJpaRepository
        extends JpaRepository<UserRoleJpaEntity, Long> {

    List<UserRoleJpaEntity> findAllByUser_IdOrderByAssignedAtAsc(Long userId);
}
