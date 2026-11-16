package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.RevokeConnectedApplicationCommand;
import kh.edu.istad.iam.domain.port.out.ConnectedApplicationsRepository;

import java.util.Objects;

public class RevokeConnectedApplicationUseCase {
    private final ConnectedApplicationsRepository repository;

    public RevokeConnectedApplicationUseCase(ConnectedApplicationsRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public void execute(RevokeConnectedApplicationCommand command) {
        Objects.requireNonNull(command, "Revoke connected application command cannot be null");
        if (command.clientId() == null || command.clientId().isBlank()) {
            throw new IllegalArgumentException("Client ID cannot be blank");
        }
        repository.revoke(command.clientId(), command.username(), command.userUuid());
    }
}
