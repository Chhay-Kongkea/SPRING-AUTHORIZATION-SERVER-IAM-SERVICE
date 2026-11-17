package kh.edu.istad.iam.infrastructure.adapter;

import kh.edu.istad.iam.domain.port.out.ClientDirectory;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RegisteredClientDirectory implements ClientDirectory {

    private final RegisteredClientRepository clients;

    public RegisteredClientDirectory(RegisteredClientRepository clients) {
        this.clients = clients;
    }

    @Override
    public Optional<String> findClientName(String clientId) {
        RegisteredClient client = clients.findByClientId(clientId);
        return client == null ? Optional.empty() : Optional.ofNullable(client.getClientName());
    }
}
