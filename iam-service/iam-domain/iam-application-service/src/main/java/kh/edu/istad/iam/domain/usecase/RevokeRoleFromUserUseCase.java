package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.RevokeRoleFromUserCommand;
import kh.edu.istad.iam.domain.entity.User;
import kh.edu.istad.iam.domain.exception.NotFoundDomainException;
import kh.edu.istad.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.iam.domain.port.out.UserRepository;
import kh.edu.istad.iam.domain.valueobject.RoleId;
import kh.edu.istad.iam.domain.valueobject.UserId;

import java.util.Objects;

public class RevokeRoleFromUserUseCase {

    private final UserRepository userRepository;
    private final CurrentActorProvider currentActorProvider;

    public RevokeRoleFromUserUseCase(
            UserRepository userRepository,
            CurrentActorProvider currentActorProvider
    ) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.currentActorProvider = Objects.requireNonNull(currentActorProvider);
    }

    public void execute(RevokeRoleFromUserCommand command) {

        Objects.requireNonNull(command, "Command cannot be null");
        Objects.requireNonNull(command.userId(), "User ID cannot be null");
        Objects.requireNonNull(command.roleId(), "Role ID cannot be null");

        User user = userRepository.findById(
                new UserId(command.userId())
        ).orElseThrow(() -> new NotFoundDomainException(
                "User not found: " + command.userId()
        ));

        String actor = currentActorProvider.getCurrentActor();

        user.revokeRole(
                new RoleId(command.roleId()),
                actor,
                command.reason()
        );

        userRepository.save(user);
    }
}