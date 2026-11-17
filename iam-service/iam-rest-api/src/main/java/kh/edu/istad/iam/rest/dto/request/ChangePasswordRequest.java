package kh.edu.istad.iam.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kh.edu.istad.iam.rest.dto.PasswordRules;

public record ChangePasswordRequest(
        @NotBlank String currentPassword,
        @NotBlank
        @Pattern(regexp = PasswordRules.COMPLEXITY, message = PasswordRules.MESSAGE)
        String newPassword,
        @NotBlank String confirmPassword
) {
}
