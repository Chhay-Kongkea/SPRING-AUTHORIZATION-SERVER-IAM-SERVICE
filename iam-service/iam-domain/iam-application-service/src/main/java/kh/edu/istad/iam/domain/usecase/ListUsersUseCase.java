package kh.edu.istad.iam.domain.usecase;



import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.result.UserSummaryResult;

import java.util.Objects;

public class ListUsersUseCase {

    private final UserRepository userRepository;

    public ListUsersUseCase(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public PageResult<UserSummaryResult> execute(PageQuery query) {

        Objects.requireNonNull(query, "Page query cannot be null");

        return userRepository.findAll(query);
    }
}