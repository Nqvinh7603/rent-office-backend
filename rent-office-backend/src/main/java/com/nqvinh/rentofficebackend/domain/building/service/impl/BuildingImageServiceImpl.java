package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingImage;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingImageService;
import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BuildingImageServiceImpl implements BuildingImageService {
    ImageService imageService;

    @Override
    @SneakyThrows
    public void updateBuildingImages(Building building, List<MultipartFile> buildingImages){
        List<String> existingUrls = building.getBuildingImages().stream()
                .map(BuildingImage::getImgUrl)
                .collect(Collectors.toList());
        List<String> uploadedUrls = imageService.handleImageUpload(buildingImages, existingUrls).get();
        uploadedUrls.forEach(url -> {
            if (building.getBuildingImages().stream().noneMatch(image -> image.getImgUrl().equals(url))) {
                building.getBuildingImages().add(BuildingImage.builder()
                        .imgUrl(url)
                        .building(building)
                        .build());
            }
        });
    }
}
