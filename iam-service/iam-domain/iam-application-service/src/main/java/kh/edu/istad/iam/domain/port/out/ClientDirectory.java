package kh.edu.istad.iam.domain.port.out;

import java.util.Optional;

public interface ClientDirectory {

    Optional<String> findClientName(String clientId);
}
