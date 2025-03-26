/*******************************************************************************
 * Class        ：AppointmentController
 * Created date ：2025/03/25
 * Lasted date  ：2025/03/25
 * Author       ：vinhNQ2
 * Change log   ：2025/03/25：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.application.api.appointment;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.CustomerAppointmentReqDto;
import com.nqvinh.rentofficebackend.domain.building.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AppointmentController
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@RestController
@RequestMapping(UrlConstant.APPOINTMENTS)
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AppointmentController {

    AppointmentService appointmentService;

    @PostMapping
    public ApiResponse<Void> createAppointment(@RequestBody CustomerAppointmentReqDto appointmentDto) {
        appointmentService.createAppointment(appointmentDto);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Appointment"))
                .build();
    }

}