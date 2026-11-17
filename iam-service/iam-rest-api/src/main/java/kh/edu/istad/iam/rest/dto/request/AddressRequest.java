package kh.edu.istad.iam.rest.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AddressRequest(
            @Size(max = 255)
            String streetAddress,
            @Size(max = 100)
            String locality,
            @Size(max = 100)
            String region,
            @Size(max = 20)
            String postalCode,
            @Pattern(regexp = "[A-Z]{2}",
                    message = "must be an ISO 3166-1 alpha-2 code")
            String country
    ) {}