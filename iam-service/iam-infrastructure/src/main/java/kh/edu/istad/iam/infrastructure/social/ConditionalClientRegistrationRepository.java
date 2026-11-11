package kh.edu.istad.iam.infrastructure.social;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ConditionalClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {
    private final Map<String, ClientRegistration> registrations;

    public ConditionalClientRegistrationRepository(List<ClientRegistration> registrations) {
        Map<String, ClientRegistration> indexed = new LinkedHashMap<>();
        for (ClientRegistration registration : registrations) {
            indexed.put(registration.getRegistrationId(), registration);
        }
        this.registrations = Map.copyOf(indexed);
    }

    @Override
    public ClientRegistration findByRegistrationId(String registrationId) {
        return registrations.get(registrationId);
    }

    @Override
    public Iterator<ClientRegistration> iterator() {
        return registrations.values().iterator();
    }
}
