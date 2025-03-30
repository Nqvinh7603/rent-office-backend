/*******************************************************************************
 * Class        ：AppointmentBuildingMapper
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.appointment.calendar;

import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar.AppointmentBuildingCalendarDto;
import com.nqvinh.rentofficebackend.domain.building.entity.AppointmentBuilding;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * AppointmentBuildingMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {AppointmentBuildingStatusHistoryCalendarMapper.class, BuildingMapper.class, AppointmentCalendarMapper.class})
public interface AppointmentBuildingCalendarMapper extends CommonMapper<AppointmentBuildingCalendarDto, AppointmentBuilding> {
}