package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingImageDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingImage;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingRepository;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingImageService;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingService;
import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BuildingServiceImpl implements BuildingService {

    BuildingRepository buildingRepository;
    BuildingMapper buildingMapper;
    ImageService imageService;
    PaginationUtils paginationUtils;
    BuildingImageService buildingImageService;

    @Override
    @Transactional
    @SneakyThrows
    public BuildingDto createBuilding(BuildingDto buildingDto, List<MultipartFile> buildingImages) {
        Building building = buildingMapper.toEntity(buildingDto);
        List<String> uploadedUrls = imageService.handleImageUpload(buildingImages, buildingDto
                .getBuildingImages().stream()
                .map(BuildingImageDto::getImgUrl)
                .collect(Collectors.toList())).get();

        List<BuildingImage> buildingImagesEntities = uploadedUrls.stream()
                .map(imgUrl -> BuildingImage.builder().imgUrl(imgUrl).building(building).build())
                .collect(Collectors.toList());
        building.setBuildingImages(buildingImagesEntities);

        return buildingMapper.toDto( buildingRepository.save(building));
    }

    @Override
    @Transactional
    @SneakyThrows
    public BuildingDto updateBuilding(Long buildingId, BuildingDto buildingDto, List<MultipartFile> buildingImages) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found"));

        if (buildingImages != null && !buildingImages.isEmpty()) {
            buildingImageService.updateBuildingImages(building, buildingImages);
        }

        buildingMapper.partialUpdate(building, buildingDto);

        return buildingMapper.toDto(buildingRepository.save(building));
    }

    @Override
    @SneakyThrows
    public BuildingDto getBuildingById(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found"));
        return buildingMapper.toDto(building);
    }

    @Override
    @SneakyThrows
    public void deleteBuilding(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found"));
        buildingRepository.delete(building);
    }

    @Override
    public Page<BuildingDto> getBuildings(Map<String, String> params) {
        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<Building> buildingPage = buildingRepository.findAll(pageable);
        Meta meta = paginationUtils.buildMeta(buildingPage, pageable);
        return paginationUtils.mapPage(buildingPage, meta, buildingMapper::toDto);
    }
}