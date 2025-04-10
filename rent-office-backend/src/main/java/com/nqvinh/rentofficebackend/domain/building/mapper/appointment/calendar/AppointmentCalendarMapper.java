/*******************************************************************************
 * Class        ：AppointmentMapper
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.appointment.calendar;

import com.nqvinh.rentofficebackend.domain.building.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar.AppointmentCalendarDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Appointment;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;
import com.nqvinh.rentofficebackend.domain.building.mapper.CustomerMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * AppointmentMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {AppointmentBuildingCalendarMapper.class, CustomerMapper.class})
public interface AppointmentCalendarMapper extends CommonMapper<AppointmentCalendarDto, Appointment> {
    @Override
    @Mapping(target = "customer", expression = "java(updateAppointment(entity, dto.getCustomer()))")
    void partialUpdate(@MappingTarget Appointment entity, AppointmentCalendarDto dto);



    default Customer updateAppointment(Appointment appointmentBuilding, CustomerDto customer) {
        if (customer == null || (appointmentBuilding.getCustomer() != null && appointmentBuilding.getCustomer().getCustomerId().equals(customer.getCustomerId()))) {
            return appointmentBuilding.getCustomer();
        }
        Customer newCustomer = new Customer();
        newCustomer.setCustomerId(customer.getCustomerId());
        return newCustomer;
    }
}