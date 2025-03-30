/*******************************************************************************
 * Class        ：AppointmentSerivce
 * Created date ：2025/03/25
 * Lasted date  ：2025/03/25
 * Author       ：vinhNQ2
 * Change log   ：2025/03/25：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar.AppointmentBuildingCalendarDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar.AppointmentCalendarDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.CustomerAppointmentReqDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AppointmentSerivce
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public interface AppointmentService {
    void createAppointment(CustomerAppointmentReqDto customerAppointment);
    Map<LocalDateTime, List<AppointmentBuildingCalendarDto>> getAllAppointmentCalendars();
    Page<AppointmentBuildingCalendarDto> getAllAppointmentCalendars( Map<String, String> params);
    AppointmentBuildingCalendarDto getAppointmentCalendarById(Long appointmentBuildingId);
    void deleteAppointmentCalendarById(Long appointmentBuildingId);
}