package com.nqvinh.rentofficebackend.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    String email;
    @NotBlank(message = "Site url is required")
    String siteUrl;
}
