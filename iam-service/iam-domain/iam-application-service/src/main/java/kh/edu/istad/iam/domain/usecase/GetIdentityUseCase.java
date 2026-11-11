package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.mapper.IamMapper;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.result.IdentityResult;
import kh.edu.istad.iam.domain.valueobject.IdentityId;

import java.util.UUID;

public class GetIdentityUseCase {

    private final IdentityRepository identityRepository;
    private final IamMapper iamMapper;

    public GetIdentityUseCase(
            IdentityRepository identityRepository
    ) {
        this.identityRepository = identityRepository;
        this.iamMapper = IamMapper.INSTANCE;
    }

    public IdentityResult execute(UUID identityUuid) {

        if (identityUuid == null) {
            throw new IllegalArgumentException("Identity ID is required");
        }

        Identity identity = identityRepository
                .findById(new IdentityId(identityUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("Identity not found"));

        if (identity.isDeleted()) {
            throw new IllegalArgumentException("Identity has been unlinked");
        }

        return iamMapper.toIdentityResult(identity);
    }
}