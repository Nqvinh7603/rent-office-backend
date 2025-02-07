package com.nqvinh.rentofficebackend.domain.building.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingImageDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingImage;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {BuildingMapper.class, BuildingTypeMapper.class, BuildingUnitMapper.class, FeePriceMapper.class, BuildingImageMapper.class},
        config = CommonMapperConfig.class)
public interface BuildingMapper extends CommonMapper<BuildingDto, Building> {

    @Override
    @Mapping(target = "buildingImages", expression = "java(updateBuildingImages(entity, dto.getBuildingImages()))")
    void partialUpdate(@MappingTarget Building entity, BuildingDto dto);

   default List<BuildingImage> updateBuildingImages(Building building, List<BuildingImageDto> buildingImageDto) {
        if (buildingImageDto == null || buildingImageDto.isEmpty()) {
            return building.getBuildingImages();
        }

        List<String> newUrls = buildingImageDto.stream()
                .map(BuildingImageDto::getImgUrl)
                .collect(Collectors.toList());

        building.getBuildingImages().removeIf(image -> !newUrls.contains(image.getImgUrl()));

        buildingImageDto.forEach(img -> {
            building.getBuildingImages().stream()
                    .filter(image -> image.getImgUrl().equals(img.getImgUrl()))
                    .findFirst()
                    .orElseGet(() -> {
                        BuildingImage newImage = BuildingImage.builder()
                                .buildingImageId(img.getBuildingImageId())
                                .imgUrl(img.getImgUrl())
                                .building(building)
                                .build();
                        building.getBuildingImages().add(newImage);
                        return newImage;
                    });
        });

        return building.getBuildingImages();
    }
}


