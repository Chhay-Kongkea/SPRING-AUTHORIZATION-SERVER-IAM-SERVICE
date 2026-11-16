
package kh.edu.istad.iam.persistence.repository;

import kh.edu.istad.iam.persistence.entity.RolePermissionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionJpaRepository
        extends JpaRepository<RolePermissionJpaEntity, Long> {

    List<RolePermissionJpaEntity>
    findAllByRole_IdOrderByAssignedAtAsc(Long roleId);

}
