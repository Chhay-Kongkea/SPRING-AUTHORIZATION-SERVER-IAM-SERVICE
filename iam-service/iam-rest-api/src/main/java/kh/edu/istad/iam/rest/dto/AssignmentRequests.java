package kh.edu.istad.iam.rest.dto;

import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class AssignmentRequests {

    private AssignmentRequests() {
    }

    public record RoleAssignments(
            @NotEmpty List<UUID> roleUuids,
            Instant expiresAt,
            String reason
    ) {
    }

    public record PermissionGrants(
            @NotEmpty List<UUID> permissionUuids,
            String effect,
            Instant expiresAt,
            String reason
    ) {
    }

    public record RolePermissions(
            @NotEmpty List<UUID> permissionUuids,
            Instant expiresAt,
            String reason
    ) {
    }

    public record PasswordReset(
            String newPassword
    ) {
    }

    public record ProfileUpdate(
            String givenName,
            String familyName,
            String middleName,
            String nickname,
            String gender,
            java.time.LocalDate birthdate,
            String profileUrl,
            String pictureUrl,
            String websiteUrl,
            String zoneinfo,
            String locale,
            String phoneNumber,
            String streetAddress,
            String locality,
            String region,
            String postalCode,
            String country
    ) {
    }
}