package kh.edu.istad.iam.domain.command;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserProfileCommand(
        UUID userId,
        String givenName,
        String familyName,
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
) {
}