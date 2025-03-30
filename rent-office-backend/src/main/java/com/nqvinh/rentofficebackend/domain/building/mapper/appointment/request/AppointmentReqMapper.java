/*******************************************************************************
 * Class        ：AppointmentReqMapper
 * Created date ：2025/03/27
 * Lasted date  ：2025/03/27
 * Author       ：vinhNQ2
 * Change log   ：2025/03/27：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.appointment.request;

import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.AppointmentReqDto;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import org.mapstruct.Mapper;

/**
 * AppointmentReqMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", uses = { AppointmentBuildingReqMapper.class})
public interface AppointmentReqMapper extends CommonMapper<AppointmentReqDto, AppointmentReqDto> {
}