# IAM Service — DDD + Hexagonal Architecture

Identity and Access Management service using Spring Boot 4.1.2 and Spring Security 7.1.2 Authorization Server (OAuth 2.1 / OpenID Connect 1.0).

> **Important version note:** Use Spring Security's `HttpSecurity.oauth2AuthorizationServer(...)` DSL and the Boot 4 `spring-boot-starter-security-oauth2-authorization-server` starter (with the matching `spring-boot-starter-security-oauth2-client` and `spring-boot-starter-security-oauth2-resource-server` starters). Avoid the older deprecated starter and legacy standalone configuration examples.

## Architecture

```text
iam-service
├── iam-domain
│   ├── iam-domain-core          Plain Java aggregates, value objects and domain rules
│   └── iam-application-service Use cases, commands, results and output ports
├── iam-persistence              Spring Data JPA adapters for application ports
├── iam-infrastructure           Security, Authorization Server, social identity adapters
├── iam-rest-api                 REST controllers, request DTOs, API security and Problem Details
├── iam-web                      JTE pages and web controllers
└── iam-main                     Composition root, Spring Boot and configuration
```

Dependency direction:

- `iam-domain-core` has no Spring, JPA, servlet, or OAuth dependencies.
- `iam-application-service` depends on `iam-domain-core` and declares output ports. It uses plain Java use-case classes.
- `iam-persistence`, `iam-infrastructure`, `iam-rest-api`, and `iam-web` are outer adapters.
- `iam-main` is the composition root and depends on all adapters.

## Included implementation

- Domain aggregates: `User`, `Role`, `Permission`, `Identity`, `UserRole`, `RolePermission`, and `UserPermission`.
- Separate audited assignment entities and revocation semantics; assignments are retained as history.
- Output ports for user, role, permission, identity repositories, password hashing, and current actor.
- Application use cases registered from the composition root; use-case classes themselves do not depend on Spring.
- Spring Data JPA persistence adapters, MapStruct mapping, optimistic locking on aggregate entities.
- Liquibase PostgreSQL schema including OAuth2 JDBC tables and built-in ADMIN/USER roles and permissions.
- JDBC `RegisteredClientRepository`, `OAuth2AuthorizationService`, and `OAuth2AuthorizationConsentService`.
- OAuth 2.1 Authorization Code + PKCE, Refresh Token, Client Credentials, OIDC, JWT authorities, OIDC standard claims, and the OIDC Dynamic Client Registration endpoint.
- Google (OIDC), GitHub, Facebook OAuth2 login; Telegram Login Widget callback with Telegram HMAC validation.
- REST endpoints for user, role, permission and self-service operations; RFC 9457 `ProblemDetail` error responses.
- JTE pages for login, registration, consent, profile, linked accounts and connected apps; English and Khmer message bundles. JTE templates are generated at build time by the JTE Gradle plugin for production.
- Unit tests for user assignment history, grant/deny effects and soft deletion.

## Technology versions

| Component       |                 Version |
| --------------- | ----------------------: |
| Java toolchain  |                      25 |
| Spring Boot     |                   4.1.2 |
| Spring Security | 7.1.2 (managed by Boot) |
| Gradle Wrapper  | 9.8.2 (project wrapper) |
| JTE             |                   3.2.5 |
| PostgreSQL      |                    18.4 |
| Liquibase       |            Boot-managed |

Spring Boot 4.1.2 and Spring Security 7.1.2 are stable releases as of 11-Nov-2026. JTE uses its Spring Boot 4 starter.

## 1. Configure secrets

Copy `.env.example` to `.env`. Never commit `.env`.

Required for local startup:

- `DB_PASSWORD`
- `IAM_DCR_CLIENT_SECRET`
- `IAM_WEB_CLIENT_SECRET`

Recommended before production:

- Persistent RSA key pair in `IAM_JWK_PUBLIC_KEY_B64` and `IAM_JWK_PRIVATE_KEY_B64`.
- `IAM_ADMIN_USERNAME`, `IAM_ADMIN_EMAIL`, `IAM_ADMIN_PASSWORD`.
- Provider credentials for Google, GitHub and Facebook.
- `TELEGRAM_BOT_TOKEN`.

The app generates a temporary RSA signing key only when no key pair is configured. That fallback is for local development only; tokens become invalid after restart. Configure a persistent key pair for any shared environment.

### Generate a persistent RSA key pair (PowerShell + OpenSSL)

```powershell
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out private.pem
openssl pkey -in private.pem -pubout -out public.pem
openssl pkcs8 -topk8 -nocrypt -in private.pem -outform DER -out private.der
openssl pkey -pubin -in public.pem -outform DER -out public.der

$env:IAM_JWK_PRIVATE_KEY_B64 = [Convert]::ToBase64String([IO.File]::ReadAllBytes((Resolve-Path .\private.der)))
$env:IAM_JWK_PUBLIC_KEY_B64 = [Convert]::ToBase64String([IO.File]::ReadAllBytes((Resolve-Path .\public.der)))
```

Store the resulting values in your secret manager. Never commit the DER or PEM files.

## 2. Start PostgreSQL

From the repository root:

```powershell
docker compose --env-file .env -f deployment/postgres/docker-compose.yaml up -d
```

PostgreSQL is exposed on `localhost:9544`, database `iam_db`.

## 3. Run the application

Use JDK 25 and run from the repository root:

```powershell
.\gradlew.bat -p iam-service :iam-main:bootRun
```

Application URL: `http://localhost:9000`

Health: `http://localhost:9000/actuator/health`

Liquibase runs automatically. Hibernate is configured with `ddl-auto: validate`; it does not create or mutate schema.

If you are using a different database host or port, set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.

## 4. Bootstrap administrator

Set these environment variables before first startup:

```powershell
$env:IAM_ADMIN_USERNAME = "iamadmin"
$env:IAM_ADMIN_EMAIL = "admin@example.com"
$env:IAM_ADMIN_PASSWORD = "Replace-With-A-Long-Random-Password"
```

The bootstrap runner creates the initial account only when all three values are set and the username does not already exist. It assigns the seeded `ADMIN` role. Remove or unset the bootstrap password after initial provisioning.

## 5. OAuth 2.1 and OIDC

Endpoints:

- Authorization: `GET /oauth2/authorize`
- Token: `POST /oauth2/token`
- JWK Set: `GET /oauth2/jwks`
- OIDC discovery: `GET /.well-known/openid-configuration`
- UserInfo: `GET /userinfo`
- Dynamic Client Registration: `POST /connect/register`

### Get a Dynamic Client Registration initial access token

The bootstrap client is seeded from `IAM_DCR_CLIENT_ID` and `IAM_DCR_CLIENT_SECRET`.

```bash
curl -u "$IAM_DCR_CLIENT_ID:$IAM_DCR_CLIENT_SECRET" \
  -d "grant_type=client_credentials&scope=client.create" \
  http://localhost:9000/oauth2/token
```

Use the returned `access_token` as a Bearer token:

```bash
curl -X POST http://localhost:9000/connect/register \
  -H "Authorization: Bearer <initial_access_token>" \
  -H "Content-Type: application/json" \
  -d '{
    "client_name": "ISTAD Demo Client",
    "redirect_uris": ["https://oauth.pstmn.io/v1/callback"],
    "grant_types": ["authorization_code", "refresh_token"],
    "response_types": ["code"],
    "scope": "openid profile email",
    "token_endpoint_auth_method": "client_secret_basic"
  }'
```

Use the returned `client_id` and `client_secret` in Postman. For browser/public clients, use a public client with `none` authentication and mandatory PKCE rather than embedding a client secret.

### JWT authorities

Access tokens include:

- `sub`: IAM user's public UUID
- `authorities`: `ROLE_*` and `resource:action` authorities
- `roles`: role names without the `ROLE_` prefix
- `permissions`: effective permission names

The effective permission calculation is implemented by `GetUserAuthoritiesUseCase`: active role permissions plus direct GRANT minus direct DENY.

### OIDC scopes

`openid`, `profile`, `email`, `phone`, and `address` are mapped to standard claims in ID tokens and the UserInfo response. Claims are emitted only for scopes granted to the client.

## 6. Social login setup

Set the provider credentials in environment variables:

- `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`
- `GITHUB_CLIENT_ID`, `GITHUB_CLIENT_SECRET`
- `FACEBOOK_CLIENT_ID`, `FACEBOOK_CLIENT_SECRET`
- `TELEGRAM_BOT_TOKEN`

Register these callback URLs with the providers:

- Google/GitHub/Facebook: `http://localhost:9000/login/oauth2/code/{registrationId}`
- Telegram: configure the Telegram Login Widget to call `/auth/telegram/callback`.

The login and linked-accounts pages show only providers whose client ID and secret are configured. While signed in, use `/profile/accounts` to link a provider; use the unlink action to disconnect it. The last active sign-in method cannot be unlinked. Replace `YOUR_TELEGRAM_BOT_USERNAME` in the Telegram widget markup with your bot's username.

Telegram does not provide an email address in the Login Widget payload. The callback therefore requires the user to supply an email address; it is stored as unverified until verified through a separate email-verification workflow. The callback validates the Telegram HMAC and the `auth_date` freshness window before provisioning the identity.

## 7. REST API

All `/api/**` endpoints except `/api/auth/register` require a Bearer access token.

Implemented routes include:

- `POST /api/auth/register`
- Users: create, list, get, update, delete, enable, disable, lock, unlock, reset password, authorities, assign/revoke roles, grant/deny/revoke permissions, list/unlink identities
- Self service: `GET/PATCH /api/me`, change password, list/unlink identities
- Roles: create, list, get, update, activate, deactivate, delete, assign/revoke permissions
- Permissions: create, list, get, update, activate, deactivate, delete

List routes accept `page`, `size`, `sort`, and `direction`. Error responses use Spring `ProblemDetail` with `application/problem+json`.

See `postman/IAM-Service.postman_collection.json` for the starter collection.

## 8. Run tests

```powershell
.\gradlew.bat -p iam-service test
```

## Security and production hardening checklist

- Use HTTPS and configure the issuer to the public HTTPS URL.
- Configure persistent RSA keys through a secret manager and set `IAM_ALLOW_EPHEMERAL_JWK=false`; implement overlapping JWK publication before rotating signing keys.
- Replace local-development secrets and disable default secret fallbacks.
- Configure provider redirect URIs to exact production origins.
- Add email verification, password reset token delivery, rate limiting, MFA and brute-force protections before public launch.
- Add a transactional outbox or event publication if downstream services need IAM lifecycle events.
- Use Flyway/Liquibase rollback strategy and database backups.
- Add Testcontainers integration tests and end-to-end OAuth2/OIDC tests against a disposable PostgreSQL instance.
- Keep `JTE_DEV_MODE=false` in production; the Gradle build precompiles templates from `iam-web/src/main/jte`.

