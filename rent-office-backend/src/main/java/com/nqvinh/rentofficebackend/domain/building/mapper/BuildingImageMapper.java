package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingImageDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingImage;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", config = CommonMapperConfig.class)
public interface BuildingImageMapper extends CommonMapper<BuildingImageDto, BuildingImage> {

}
