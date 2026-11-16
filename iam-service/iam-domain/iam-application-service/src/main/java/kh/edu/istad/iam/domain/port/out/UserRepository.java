package kh.edu.istad.iam.domain.port.out;


import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.result.UserSummaryResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(UserId userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    PageResult<UserSummaryResult> findAll(PageQuery query);
}