package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingLevelDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingLevel;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingLevelMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingLevelRepository;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingLevelService;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BuildingLevelServiceImpl implements BuildingLevelService {
    BuildingLevelRepository buildingLevelRepository;
    BuildingLevelMapper buildingLevelMapper;
    PaginationUtils paginationUtils;

    @Override
    @Transactional
    public BuildingLevelDto createBuildingLevel(BuildingLevelDto buildingLevelDto) {
        BuildingLevel buildingLevel = buildingLevelMapper.toEntity(buildingLevelDto);
        buildingLevel = buildingLevelRepository.save(buildingLevel);
        return buildingLevelMapper.toDto(buildingLevel);
    }

    @Override
    @SneakyThrows
    @Transactional
    public BuildingLevelDto updateBuildingLevel(Long buildingLevelId, BuildingLevelDto buildingLevelDto) {
        BuildingLevel buildingLevel = buildingLevelRepository.findById(buildingLevelId)
                .orElseThrow(() -> new ResourceNotFoundException("Building level not found"));
        buildingLevelMapper.partialUpdate(buildingLevel, buildingLevelDto);
        return buildingLevelMapper.toDto(buildingLevelRepository.save(buildingLevel));
    }

    @Override
    @SneakyThrows
    public void deleteBuildingLevel(Long id) {
        BuildingLevel buildingLevel = buildingLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Building level not found"));
        buildingLevelRepository.delete(buildingLevel);
    }

    @Override
    public List<BuildingLevelDto> getAllBuildingLevels() {
        return buildingLevelMapper.toDtoList(buildingLevelRepository.findAll());
    }


    @Override
    public Page<BuildingLevelDto> getBuildingLevels(Map<String, String> params) {
        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<BuildingLevel> buildingLevelPage = buildingLevelRepository.findAll(pageable);
        Meta meta = paginationUtils.buildMeta(buildingLevelPage, pageable);
        return paginationUtils.mapPage(buildingLevelPage, meta, buildingLevelMapper::toDto);
    }
}
