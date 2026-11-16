package kh.edu.istad.iam.domain.port.out;



import kh.edu.istad.iam.domain.result.ConnectedApplicationResult;

import java.util.List;

public interface ConnectedApplicationsRepository {

    List<ConnectedApplicationResult> findByPrincipalNames(String username, String userUuid);

    void revoke(String clientId, String username, String userUuid);
}
