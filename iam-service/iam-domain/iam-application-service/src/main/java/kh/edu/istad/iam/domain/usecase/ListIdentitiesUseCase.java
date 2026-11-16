package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.result.IdentityResult;
import kh.edu.istad.iam.domain.result.PageResult;

public class ListIdentitiesUseCase {

    private final IdentityRepository identityRepository;
    private final IamMapper iamMapper;

    public ListIdentitiesUseCase(
            IdentityRepository identityRepository
    ) {
        this.identityRepository = identityRepository;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public PageResult<IdentityResult> execute(PageQuery query) {

        if (query == null) {
            throw new IllegalArgumentException("Page query is required");
        }

        PageResult<Identity> identities = identityRepository.findAll(query);

        return new PageResult<>(
                identities.content().stream()
                        .filter(identity -> !identity.isDeleted())
                        .map(iamMapper::toIdentityResult)
                        .toList(),
                identities.page(),
                identities.size(),
                identities.totalElements(),
                identities.totalPages(),
                identities.first(),
                identities.last()
        );
    }
}