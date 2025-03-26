/*******************************************************************************
 * Class        ：AppointmentSerivce
 * Created date ：2025/03/25
 * Lasted date  ：2025/03/25
 * Author       ：vinhNQ2
 * Change log   ：2025/03/25：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.service;

import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.CustomerAppointmentReqDto;

/**
 * AppointmentSerivce
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public interface AppointmentService {
    void createAppointment(CustomerAppointmentReqDto customerAppointment);
}