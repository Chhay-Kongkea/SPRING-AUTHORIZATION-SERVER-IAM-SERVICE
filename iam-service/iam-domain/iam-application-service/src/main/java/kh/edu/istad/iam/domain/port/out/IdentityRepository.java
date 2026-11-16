package kh.edu.istad.iam.domain.port.out;

import kh.edu.istad.iam.domain.command.PageQuery;
import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.result.PageResult;
import kh.edu.istad.iam.domain.valueobject.IdentityId;
import kh.edu.istad.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.List;
import java.util.Optional;

public interface IdentityRepository {

    Identity save(Identity identity);

    Optional<Identity> findById(IdentityId identityId);

    Optional<Identity> findByProviderAndSubject(
            IdentityProvider provider,
            String providerSubject
    );

    List<Identity> findAllByUserId(UserId userId);

    boolean existsByProviderAndSubject(
            IdentityProvider provider,
            String providerSubject
    );

    PageResult<Identity> findAll(PageQuery query);

    long countActiveByUserId(UserId userId);
}