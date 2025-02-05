package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingUnitDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingUnit;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BuildingUnitMapper.class}, config = CommonMapperConfig.class)
public interface BuildingUnitMapper extends CommonMapper<BuildingUnitDto, BuildingUnit> {
}
