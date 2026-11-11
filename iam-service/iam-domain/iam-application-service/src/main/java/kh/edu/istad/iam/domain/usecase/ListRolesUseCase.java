package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Role;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.RoleRepository;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.result.RoleSummaryResult;

import java.util.List;

public class ListRolesUseCase {

    private final RoleRepository roleRepository;
    private final IamMapper iamMapper;

    public ListRolesUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public PageResult<RoleSummaryResult> execute(PageQuery query) {

        if (query == null) {
            throw new IllegalArgumentException("Page query is required");
        }

        PageResult<Role> rolePage = roleRepository.findAll(query);

        List<RoleSummaryResult> summaries = rolePage.content()
                .stream()
                .map(iamMapper::toRoleSummaryResult)
                .toList();

        return new PageResult<>(
                summaries,
                rolePage.page(),
                rolePage.size(),
                rolePage.totalElements(),
                rolePage.totalPages(),
                rolePage.first(),
                rolePage.last()
        );
    }
}