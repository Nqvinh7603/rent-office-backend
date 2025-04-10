/*******************************************************************************
 * Class        ：AppointmentBuildingReqMapper
 * Created date ：2025/03/27
 * Lasted date  ：2025/03/27
 * Author       ：vinhNQ2
 * Change log   ：2025/03/27：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.appointment.request;

import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.AppointmentBuildingReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.AppointmentBuilding;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * AppointmentBuildingReqMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {AppointmentBuildingStatusHistoryReqMapper.class, BuildingMapper.class})
public interface AppointmentBuildingReqMapper extends CommonMapper<AppointmentBuildingReqDto, AppointmentBuilding> {
}