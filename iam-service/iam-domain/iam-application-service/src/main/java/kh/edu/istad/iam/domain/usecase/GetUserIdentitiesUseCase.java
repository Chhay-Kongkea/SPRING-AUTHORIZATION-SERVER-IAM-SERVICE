package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.Identity;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.port.out.IdentityRepository;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.IdentityResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.List;
import java.util.UUID;

public class GetUserIdentitiesUseCase {

    private final UserRepository userRepository;
    private final IdentityRepository identityRepository;

    public GetUserIdentitiesUseCase(
            UserRepository userRepository,
            IdentityRepository identityRepository
    ) {
        this.userRepository = userRepository;
        this.identityRepository = identityRepository;
    }

    public List<IdentityResult> execute(UUID userUuid) {

        if (userUuid == null) {
            throw new IllegalArgumentException("User ID is required");
        }

        User user = userRepository.findById(new UserId(userUuid))
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found: " + userUuid)
                );

        return identityRepository.findAllByUserId(user.getId())
                .stream()
                .map(this::toResult)
                .toList();
    }

    private IdentityResult toResult(Identity identity) {

        return new IdentityResult(
                identity.getId().value(),
                identity.getUserId(),
                identity.getProvider(),
                identity.getProviderSubject(),
                identity.getEmail(),
                identity.isEmailVerified(),
                identity.getDisplayName(),
                identity.getUsername(),
                identity.getPictureUrl(),
                identity.getLinkedAt(),
                identity.getLastLoginAt()
        );
    }
}