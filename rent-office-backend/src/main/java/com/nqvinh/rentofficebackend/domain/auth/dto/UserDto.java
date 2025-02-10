package com.nqvinh.rentofficebackend.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    @JsonAlias({"user_id", "userId"})
    UUID userId;

    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "Password is required")
    String password;


    @JsonAlias({"first_name", "firstName"})
    @NotBlank(message = "First name is required")
    String firstName;

    @JsonAlias({"last_name", "lastName"})
    @NotBlank(message = "Last name is required")
    String lastName;

    @NotBlank(message = "Gender is required")
    String gender;

    @JsonAlias({"phone_number", "phoneNumber"})
    @NotBlank(message = "Phone number is required")
    String phoneNumber;

    boolean active;

    @JsonAlias({"avatar_url", "avatarUrl"})
    String avatarUrl;

    @JsonAlias({"date_of_birth", "dateOfBirth"})
    LocalDate dateOfBirth;

    @NotNull(message = "Role are required")
    RoleDto role;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;

    String checked;
}