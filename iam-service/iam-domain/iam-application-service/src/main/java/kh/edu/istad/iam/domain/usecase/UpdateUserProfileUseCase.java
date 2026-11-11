package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.UpdateUserProfileCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.result.UserResult;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Objects;

public class UpdateUserProfileUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;

    public UpdateUserProfileUseCase(UserRepository userRepository, CurrentActorProvider currentActorProvider) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public UserResult execute(UpdateUserProfileCommand command) {

        Objects.requireNonNull(command, "Update profile command cannot be null");
        Objects.requireNonNull(command.userId(), "User ID cannot be null");

        User user = userRepository.findById(new UserId(command.userId()))
                .orElseThrow(() ->
                        new NotFoundDomainException(
                                "User not found: " + command.userId()
                        )
                );

        user.updateProfile(
                command.givenName(),
                command.familyName(),
                command.middleName(),
                command.nickname(),
                command.gender(),
                command.birthdate(),
                command.profileUrl(),
                command.pictureUrl(),
                command.websiteUrl(),
                command.zoneInfo(),
                command.locale(),
                command.phoneNumber(),
                command.streetAddress(),
                command.locality(),
                command.region(),
                command.postalCode(),
                command.country(),
                currentActorProvider.getCurrentActor()
        );

        User updatedUser = userRepository.save(user);

        return new UserResult(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.isEmailVerified(),
                updatedUser.getGivenName(),
                updatedUser.getFamilyName(),
                updatedUser.getLocale(),
                updatedUser.isEnabled(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt()
        );
    }
}