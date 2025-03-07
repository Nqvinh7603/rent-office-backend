/*******************************************************************************
 * Class        ：ConsignmentStatusHistory
 * Created date ：2025/03/04
 * Lasted date  ：2025/03/04
 * Author       ：vinhNQ2
 * Change log   ：2025/03/04：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.customer.mapper;

import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentStatusHistoryDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.ConsignmentStatusHistory;
import org.mapstruct.Mapper;

/**
 * ConsignmentStatusHistory
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface ConsignmentStatusHistoryMapper extends CommonMapper<ConsignmentStatusHistoryDto, ConsignmentStatusHistory> {
}