package kh.edu.istad.iam.infrastructure.adapter;

import kh.edu.istad.iam.domain.port.out.ConnectedApplicationsRepository;
import kh.edu.istad.iam.domain.result.ConnectedApplicationResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public final class JdbcConnectedApplicationsRepository implements ConnectedApplicationsRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcConnectedApplicationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ConnectedApplicationResult> findByPrincipalNames(String username, String userUuid) {
        return jdbcTemplate.query("""
                SELECT c.id, c.client_name, c.scopes
                FROM oauth2_authorization_consent a
                JOIN oauth2_registered_client c ON c.id = a.registered_client_id
                WHERE a.principal_name IN (?, ?)
                ORDER BY c.client_name
                """, (rs, rowNum) -> new ConnectedApplicationResult(
                rs.getString("id"), rs.getString("client_name"), rs.getString("scopes")),
                username, userUuid);
    }

    @Override
    public void revoke(String clientId, String username, String userUuid) {
        jdbcTemplate.update("DELETE FROM oauth2_authorization WHERE registered_client_id = ? AND principal_name IN (?, ?)",
                clientId, username, userUuid);
        jdbcTemplate.update("DELETE FROM oauth2_authorization_consent WHERE registered_client_id = ? AND principal_name IN (?, ?)",
                clientId, username, userUuid);
    }
}
