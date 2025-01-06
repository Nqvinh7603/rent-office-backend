package com.nqvinh.rentofficebackend.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionDto {

    Long permissionId;

    @NotBlank(message = "Permission name is required")
    String name;

    @NotBlank(message = "API path is required")
    String apiPath;

    @NotBlank(message = "Method is required")
    String method;

    @NotBlank(message = "Module is required")
    String module;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}
