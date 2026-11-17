package kh.edu.istad.iam.domain.usecase;



import kh.edu.istad.iam.domain.port.out.ClientDirectory;

import java.util.Objects;


public class GetClientDisplayNameUseCase {

    private final ClientDirectory clientDirectory;

    public GetClientDisplayNameUseCase(ClientDirectory clientDirectory) {
        this.clientDirectory = Objects.requireNonNull(clientDirectory);
    }

    public String execute(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("Client ID is required");
        }
        return clientDirectory.findClientName(clientId).orElse(clientId);
    }
}
