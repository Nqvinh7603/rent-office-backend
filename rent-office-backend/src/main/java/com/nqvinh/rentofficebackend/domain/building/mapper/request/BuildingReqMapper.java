package com.nqvinh.rentofficebackend.domain.building.mapper.request;


import com.nqvinh.rentofficebackend.domain.building.dto.request.BuildingReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class,
        uses = {
        BuildingImageReqMapper.class,
                ConsignmentStatusHistoryReqMapper.class,
                BuildingTypeReqMapper.class,
                RentalPricingReqMapper.class,
                FeeReqMapper.class,
                PaymentPolicyReqMapper.class
        })
public interface BuildingReqMapper extends CommonMapper<BuildingReqDto, Building> {
}
