/*******************************************************************************
 * Class        ：AppointmentBuildingReqDto
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * AppointmentBuildingReqDto
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
public class AppointmentBuildingReqDto {
    Long appointmentBuildingId;

    List<AppointmentBuildingStatusHistoryReqDto> appointmentBuildingStatusHistories;

    LocalDateTime visitTime;

    Long buildingId;

    String area;
}