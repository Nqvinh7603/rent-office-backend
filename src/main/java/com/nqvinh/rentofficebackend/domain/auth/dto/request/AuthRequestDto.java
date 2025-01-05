package com.nqvinh.rentofficebackend.domain.auth.dto.request;

import com.nqvinh.rentofficebackend.domain.common.annotation.GenericValidation;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@GenericValidation(type = GenericValidation.ValidationType.USERNAME_OR_EMAIL)
public class AuthRequestDto {
    String username;
    String email;
    @NotNull
    String password;
}

