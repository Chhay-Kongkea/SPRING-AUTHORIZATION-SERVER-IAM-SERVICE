package kh.edu.istad.iam.domain.command;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateUserCommand(
        List<UUID> roleIds,
        String username,
        String email,
        String rawPassword,
        String givenName,
        String familyName,
        boolean emailVerified,
        String middleName,
        String nickname,
        String gender,
        LocalDate birthdate,
        String profileUrl,
        String pictureUrl,
        String websiteUrl,
        String zoneInfo,
        String locale,
        String phoneNumber,
        String streetAddress,
        String locality,
        String region,
        String postalCode,
        String country
) {}
