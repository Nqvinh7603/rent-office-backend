package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingLevelDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingTypeDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingLevel;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingType;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingLevelMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingLevelRepository;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingLevelService;
import com.nqvinh.rentofficebackend.infrastructure.utils.RequestParamUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BuildingLevelServiceImpl implements BuildingLevelService {
    BuildingLevelRepository buildingLevelRepository;
    BuildingLevelMapper buildingLevelMapper;
    RequestParamUtils requestParamUtils;

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
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, BuildingLevel.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<BuildingLevel> buildingLevelPage = buildingLevelRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(buildingLevelPage.getTotalPages())
                .total(buildingLevelPage.getTotalElements())
                .build();
        return Page.<BuildingLevelDto>builder()
                .meta(meta)
                .content(buildingLevelPage.getContent().stream()
                        .map(buildingLevelMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
