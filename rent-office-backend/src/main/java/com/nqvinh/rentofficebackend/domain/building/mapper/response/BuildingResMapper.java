package com.nqvinh.rentofficebackend.domain.building.mapper.response;

import com.nqvinh.rentofficebackend.domain.building.dto.response.BuildingResDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class,
        uses = {BuildingImageResMapper.class,
                ConsignmentStatusHistoryResMapper.class,
                BuildingTypeResMapper.class,
                RentalPricingResMapper.class,
                FeeResMapper.class,
                PaymentPolicyResMapper.class
        })
public interface BuildingResMapper extends CommonMapper<BuildingResDto, Building> {
}
