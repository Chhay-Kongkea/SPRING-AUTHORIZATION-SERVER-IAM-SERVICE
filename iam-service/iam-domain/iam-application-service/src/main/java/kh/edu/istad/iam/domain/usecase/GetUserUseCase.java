package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Objects;
import java.util.UUID;

public class GetUserUseCase {

    private final UserRepository userRepository;

    public GetUserUseCase(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public UserResult execute(UUID userId) {

        Objects.requireNonNull(userId, "User ID cannot be null");

        User user = userRepository.findById(new UserId(userId))
                .orElseThrow(() ->
                        new NotFoundDomainException("User not found: " + userId)
                );

        return new UserResult(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEmailVerified(),
                user.getGivenName(),
                user.getFamilyName(),
                user.getLocale(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}