package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Objects;
import java.util.UUID;

public class EnableUserUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;

    public EnableUserUseCase(UserRepository userRepository, CurrentActorProvider currentActorProvider) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public void execute(UUID userUuid) {

        Objects.requireNonNull(userUuid, "User UUID cannot be null");

        User user = userRepository.findById(new UserId(userUuid))
                .orElseThrow(() -> new NotFoundDomainException(
                        "User not found: " + userUuid
                ));

        user.enable(currentActorProvider.getCurrentActor());

        userRepository.save(user);
    }
}