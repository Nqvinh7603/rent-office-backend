/*******************************************************************************
 * Class        ：CustomerAppointmentReqDto
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * CustomerAppointmentReqDto
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerAppointmentReqDto {
    Long customerId;

    //@NotBlank(message = "Customer name is required")
    String customerName;

    //@NotBlank(message = "Phone number is required")
    String phoneNumber;

    //@NotBlank(message = "Email is required")
    String email;

    String address;

   // @NotBlank(message = "Require type is required")
    String requireType;

    String note;

    String status;

    List<AppointmentReqDto> appointments;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;


}