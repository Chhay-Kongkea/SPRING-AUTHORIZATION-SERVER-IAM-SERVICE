package kh.edu.istad.iam;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@EnabledIfSystemProperty(named = "iam.integration", matches = "true")
class IamSecurityIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void apiRequiresBearerAuthentication() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"));
    }

    @Test
    void dynamicRegistrationRejectsTokenWithoutClientCreateScope() throws Exception {
        mockMvc.perform(post("/connect/register")
                        .with(jwt().jwt(token -> token
                                .claim("client_id", "unauthorized-client")
                                .claim("scope", "openid")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationRequest()))
                .andExpect(status().isForbidden());
    }

    @Test
    void dynamicRegistrationPassesAuthorizationWithClientCreateScope() throws Exception {
        mockMvc.perform(post("/connect/register")
                        .with(jwt().jwt(token -> token
                                .claim("client_id", "iam-dcr-bootstrap")
                                .claim("scope", "client.create")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationRequest()))
                .andExpect(result -> {
                    if (result.getResponse().getStatus() == 403) {
                        throw new AssertionError("client.create token was denied by the DCR guard");
                    }
                });
    }

    private static String registrationRequest() {
        return """
                {
                  "client_name": "Integration Test Client",
                  "redirect_uris": ["https://client.example.test/callback"],
                  "grant_types": ["authorization_code", "refresh_token"],
                  "response_types": ["code"],
                  "scope": "openid profile email",
                  "token_endpoint_auth_method": "client_secret_basic"
                }
                """;
    }
}
