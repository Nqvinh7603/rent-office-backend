/*******************************************************************************
 * Class        ：ConsignmentStatusHistoryResMapper
 * Created date ：2025/03/04
 * Lasted date  ：2025/03/04
 * Author       ：vinhNQ2
 * Change log   ：2025/03/04：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.response;

import com.nqvinh.rentofficebackend.domain.building.dto.response.ConsignmentStatusHistoryResDto;
import com.nqvinh.rentofficebackend.domain.building.entity.ConsignmentStatusHistory;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * ConsignmentStatusHistoryResMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface ConsignmentStatusHistoryResMapper extends CommonMapper<ConsignmentStatusHistoryResDto, ConsignmentStatusHistory> {
}