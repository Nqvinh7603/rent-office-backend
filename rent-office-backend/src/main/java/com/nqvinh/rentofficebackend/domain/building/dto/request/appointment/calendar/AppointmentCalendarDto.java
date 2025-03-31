/*******************************************************************************
 * Class        ：AppointmentDto
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar;

import com.nqvinh.rentofficebackend.domain.building.dto.CustomerDto;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * AppointmentDto
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
public class AppointmentCalendarDto {
    Long appointmentId;

//    @NotNull(message = "Appointment is required")
//    List<AppointmentBuildingReqDto> appointmentBuildings;

    CustomerDto customer;


    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;
}