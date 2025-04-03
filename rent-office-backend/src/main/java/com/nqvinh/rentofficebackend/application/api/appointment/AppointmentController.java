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
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar.AppointmentBuildingCalendarDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.CustomerAppointmentReqDto;
import com.nqvinh.rentofficebackend.domain.building.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @GetMapping(UrlConstant.GET_APPOINTMENTS_CALENDAR)
    public ApiResponse<Map<LocalDateTime, List<AppointmentBuildingCalendarDto>>> getAllAppointmentCalendars() {
        return ApiResponse.<Map<LocalDateTime, List<AppointmentBuildingCalendarDto>>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Appointment"))
                .payload(appointmentService.getAllAppointmentCalendars())
                .build();
    }

    @GetMapping(UrlConstant.GET_APPOINTMENTS_CALENDAR_BY_ID)
    public ApiResponse<AppointmentBuildingCalendarDto> getAppointmentCalendarById(
            @PathVariable Long id) {
        return ApiResponse.<AppointmentBuildingCalendarDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Appointment"))
                .payload(appointmentService.getAppointmentCalendarById(id))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<AppointmentBuildingCalendarDto>> getAllAppointmentCalendars(
            @RequestParam Map<String, String> params) {
        return ApiResponse.<Page<AppointmentBuildingCalendarDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Appointment"))
                .payload(appointmentService.getAllAppointmentCalendars(params))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_APPOINTMENT_CALENDAR)
    public ApiResponse<Void> deleteAppointmentCalendarById(
            @PathVariable Long id) {
        appointmentService.deleteAppointmentCalendarById(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Appointment"))
                .build();
    }

    @PostMapping(UrlConstant.CREATE_APPOINTMENT_CALENDAR)
    public ApiResponse<Void> createPotentialCustomer(@RequestBody CustomerAppointmentReqDto customerDto) {
        appointmentService.createAppointmentAdmin(customerDto);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Potential customer"))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_APPOINTMENT_CALENDAR)
    public ApiResponse<AppointmentBuildingCalendarDto> updateAppointmentCalendarById(
            @PathVariable Long id,
            @RequestBody AppointmentBuildingCalendarDto appointmentBuildingCalendarDto) {
        return ApiResponse.<AppointmentBuildingCalendarDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Appointment"))
                .payload(appointmentService.updateAppointmentCalendarById(id, appointmentBuildingCalendarDto))
                .build();
    }

}