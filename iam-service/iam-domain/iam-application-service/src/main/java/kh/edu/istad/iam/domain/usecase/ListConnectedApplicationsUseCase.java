package kh.edu.istad.iam.domain.usecase;

import kh.edu.istad.iam.domain.command.ConnectedApplicationsQuery;
import kh.edu.istad.iam.domain.port.out.ConnectedApplicationsRepository;
import kh.edu.istad.iam.domain.result.ConnectedApplicationResult;

import java.util.List;
import java.util.Objects;

public class ListConnectedApplicationsUseCase {
    private final ConnectedApplicationsRepository repository;

    public ListConnectedApplicationsUseCase(ConnectedApplicationsRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public List<ConnectedApplicationResult> execute(ConnectedApplicationsQuery query) {
        Objects.requireNonNull(query, "Connected applications query cannot be null");
        return repository.findByPrincipalNames(query.username(), query.userUuid());
    }
}
