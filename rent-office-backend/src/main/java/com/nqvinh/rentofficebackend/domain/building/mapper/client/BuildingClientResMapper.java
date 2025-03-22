/*******************************************************************************
 * Class        ：BuildingClientResMapper
 * Created date ：2025/03/20
 * Lasted date  ：2025/03/20
 * Author       ：vinhNQ2
 * Change log   ：2025/03/20：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.mapper.client;

import com.nqvinh.rentofficebackend.domain.building.dto.response.client.BuildingClientRes;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.building.mapper.*;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

/**
 * BuildingClientResMapper
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Mapper(componentModel = "spring", config = CommonMapperConfig.class, uses = {
        BuildingImageMapper.class,
        CustomerMapper.class,
        ConsignmentStatusHistoryMapper.class,
        RentalPricingMapper.class,
        BuildingTypeMapper.class,
        FeeMapper.class,
        PaymentPolicyMapper.class,
        BuildingDetailMapper.class,
        BuildingUnitMapper.class,
})
public interface BuildingClientResMapper extends CommonMapper<BuildingClientRes, Building> {
}