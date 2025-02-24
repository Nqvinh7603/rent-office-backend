package com.nqvinh.rentofficebackend.domain.customer.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerResDto {
    Long customerId;

    @NotBlank(message = "Customer name is required")
    String customerName;

    @NotBlank(message = "Phone number is required")
    String phoneNumber;

    @NotBlank(message = "Email is required")
    String email;

    @NotBlank(message = "Address is required")
    String address;

    @NotBlank(message = "Require type is required")
    String requireType;

    @NotNull(message = "Consignment is required")
    List<ConsignmentResDto> consignments;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}
