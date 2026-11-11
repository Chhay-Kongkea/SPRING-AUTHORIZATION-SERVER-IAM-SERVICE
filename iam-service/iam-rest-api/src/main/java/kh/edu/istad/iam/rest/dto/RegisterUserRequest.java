package kh.edu.istad.iam.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record RegisterUserRequest(
        @NotBlank
        @Size(min = 3, max = 50)
        String username,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @NotBlank
        @Size(min = 12, max = 128)
        String password,

        @NotBlank
        String confirmPassword,

        @NotBlank
        @Size(max = 100)
        String givenName,

        @NotBlank
        @Size(max = 100)
        String familyName,

        @NotBlank
        @Pattern(regexp = "en|km")
        String locale
) {
}