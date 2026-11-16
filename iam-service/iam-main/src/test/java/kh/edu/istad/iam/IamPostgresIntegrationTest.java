package kh.edu.istad.iam;

import kh.edu.istad.iam.domain.command.RegisterUserCommand;
import kh.edu.istad.iam.domain.usecase.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnabledIfSystemProperty(named = "iam.integration", matches = "true")
class IamPostgresIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    RegisterUserUseCase registerUser;

    @Test
    void liquibaseCreatesIamAndAuthorizationServerTables() {
        Integer count = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM information_schema.tables
                WHERE table_schema = current_schema()
                  AND table_name IN (
                    'users', 'roles', 'permissions', 'identities',
                    'user_roles', 'role_permissions', 'user_permissions',
                    'oauth2_registered_client', 'oauth2_authorization',
                    'oauth2_authorization_consent'
                  )
                """, Integer.class);

        assertThat(count).isEqualTo(10);
    }

    @Test
    @Transactional
    void registrationPersistsUserAndDefaultUserRoleAssignment() {
        String suffix = UUID.randomUUID().toString().substring(0, 8);
        var user = registerUser.execute(new RegisterUserCommand(
                "it_" + suffix,
                "it_" + suffix + "@example.test",
                "A-Strong-Integration-Password-2026",
                "Integration",
                "Test",
                "en"
        ));

        assertThat(user.id()).isNotNull();
        Integer assignments = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM user_roles ur
                JOIN users u ON u.id = ur.user_id
                JOIN roles r ON r.id = ur.role_id
                WHERE u.uuid = ? AND r.name = 'USER' AND ur.revoked_at IS NULL
                """, Integer.class, user.id().value());

        assertThat(assignments).isEqualTo(1);
    }
}
