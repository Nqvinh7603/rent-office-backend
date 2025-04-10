package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingLevelDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingTypeDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingLevel;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingType;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class,
        uses = {
                BuildingImageMapper.class,
                CustomerMapper.class,
                ConsignmentStatusHistoryMapper.class,
//                RentalPricingMapper.class,
                BuildingTypeMapper.class,
                BuildingLevelMapper.class,
                FeeMapper.class,
                PaymentPolicyMapper.class,
                BuildingUnitMapper.class,
        })
public interface BuildingMapper extends CommonMapper<BuildingDto, Building> {

    @Override
    @Mapping(target = "consignmentStatusHistories", ignore = true)
//    @Mapping(target = "rentalPricing", ignore = true)
    @Mapping(target = "paymentPolicies", ignore = true)
    @Mapping(target = "fees", ignore = true)
    @Mapping(target = "buildingUnits", ignore = true)
    @Mapping(target = "buildingType", expression = "java(updateBuildingType(entity, dto.getBuildingType()))")
    @Mapping(target = "buildingLevel", expression = "java(updateBuildingLevel(entity, dto.getBuildingLevel()))")
    void partialUpdate(@MappingTarget Building entity, BuildingDto dto);


    default BuildingType updateBuildingType(Building building, BuildingTypeDto buildingTypeDto) {
        if (buildingTypeDto == null || (building.getBuildingType() != null && building.getBuildingType().getBuildingTypeId().equals(buildingTypeDto.getBuildingTypeId()))) {
            return building.getBuildingType();
        }
        BuildingType newBuildingType = new BuildingType();
        newBuildingType.setBuildingTypeId(buildingTypeDto.getBuildingTypeId());
        return newBuildingType;
    }

    default BuildingLevel updateBuildingLevel(Building building, BuildingLevelDto buildingLevelDto) {
        if (buildingLevelDto == null || (building.getBuildingLevel() != null && building.getBuildingLevel().getBuildingLevelId().equals(buildingLevelDto.getBuildingLevelId()))) {
            return building.getBuildingLevel();
        }
        BuildingLevel newBuildingLevel = new BuildingLevel();
        newBuildingLevel.setBuildingLevelId(buildingLevelDto.getBuildingLevelId());
        return newBuildingLevel;
    }

}
