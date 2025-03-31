/*******************************************************************************
 * Class        ：AppointmentBuildingMapper
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.appointment.calendar;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingTypeDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar.AppointmentBuildingCalendarDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar.AppointmentCalendarDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Appointment;
import com.nqvinh.rentofficebackend.domain.building.entity.AppointmentBuilding;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingType;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * AppointmentBuildingMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {AppointmentBuildingStatusHistoryCalendarMapper.class, BuildingMapper.class, AppointmentCalendarMapper.class})
public interface AppointmentBuildingCalendarMapper extends CommonMapper<AppointmentBuildingCalendarDto, AppointmentBuilding> {

    @Override
    @Mapping(target = "appointmentBuildingStatusHistories", ignore = true)
    @Mapping(target = "building", expression = "java(updateBuilding(entity, dto.getBuilding()))")
    @Mapping(target = "appointment", expression = "java(updateAppointment(entity, dto.getAppointment()))")
    void partialUpdate(@MappingTarget AppointmentBuilding entity, AppointmentBuildingCalendarDto dto);

    default Building updateBuilding(AppointmentBuilding appointmentBuilding, BuildingDto buildingDto) {
        if (buildingDto == null || (appointmentBuilding.getBuilding() != null && appointmentBuilding.getBuilding().getBuildingId().equals(buildingDto.getBuildingId()))) {
            return appointmentBuilding.getBuilding();
        }
        Building newBuilding = new Building();
        newBuilding.setBuildingId(buildingDto.getBuildingId());
        return newBuilding;
    }

    default Appointment updateAppointment(AppointmentBuilding appointmentBuilding, AppointmentCalendarDto appointment) {
        if (appointment == null || (appointmentBuilding.getAppointment() != null && appointmentBuilding.getAppointment().getAppointmentId().equals(appointment.getAppointmentId()))) {
            return appointmentBuilding.getAppointment();
        }
        Appointment newAppointment = new Appointment();
        newAppointment.setAppointmentId(appointment.getAppointmentId());
        return newAppointment;
    }


}