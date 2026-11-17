package kh.edu.istad.iam.rest.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public record UpdateUserProfileRequest(
        @Size(max = 100)
        String givenName,
        @Size(max = 100)
        String familyName,
        @Size(max = 100)
        String middleName,
        @Size(max = 100)
        String nickname,
        @Pattern(regexp = "female|male|other",
                 message = "must be female, male or other")
        String gender,
        LocalDate birthdate,
        @Size(max = 500)
        String profileUrl,
        @Size(max = 500)
        String pictureUrl,
        @Size(max = 500)
        String websiteUrl,
        @Size(max = 50)
        String zoneinfo,
        @Pattern(regexp = "en|km",
                message = "must be en or km")
        String locale,
        @Pattern(regexp = "^\\+[1-9][0-9]{1,14}$",
                message = "must be in E.164 format")
        String phoneNumber,
        @Valid AddressRequest
        address
) {
}
