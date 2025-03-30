/*******************************************************************************
 * Class        ：AppointmentBuildingStatusHistorReqMapper
 * Created date ：2025/03/27
 * Lasted date  ：2025/03/27
 * Author       ：vinhNQ2
 * Change log   ：2025/03/27：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.appointment.request;

import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.AppointmentBuildingStatusHistoryReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.AppointmentBuildingStatusHistory;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * AppointmentBuildingStatusHistorReqMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface AppointmentBuildingStatusHistoryReqMapper extends CommonMapper<AppointmentBuildingStatusHistoryReqDto, AppointmentBuildingStatusHistory> {
}