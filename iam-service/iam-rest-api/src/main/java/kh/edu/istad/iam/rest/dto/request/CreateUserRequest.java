package kh.edu.istad.iam.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import kh.edu.istad.iam.rest.dto.PasswordRules;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateUserRequest(
        @NotBlank
        @Size(min = 3, max = 50)
        String username,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotBlank
        @Pattern(regexp = PasswordRules.COMPLEXITY, message = PasswordRules.MESSAGE)
        String password,

        @NotBlank
        @Size(max = 100)
        String givenName,

        @NotBlank
        @Size(max = 100)
        String familyName,

        String middleName,
        String nickname,
        String gender,
        LocalDate birthdate,
        String profileUrl,
        String pictureUrl,
        String websiteUrl,
        String zoneinfo,

        @Pattern(regexp = "en|km")
        String locale,

        @Pattern(regexp = "^\\+[1-9][0-9]{1,14}$", message = "must be in E.164 format")
        String phoneNumber,
        @jakarta.validation.Valid AddressRequest address,
        List<UUID> roleUuids
) {


}