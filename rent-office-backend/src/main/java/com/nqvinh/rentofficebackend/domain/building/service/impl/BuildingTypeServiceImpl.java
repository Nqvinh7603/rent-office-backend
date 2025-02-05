package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingTypeDto;
import com.nqvinh.rentofficebackend.domain.building.entity.BuildingType;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingTypeMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingTypeRepository;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingTypeService;
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
public class BuildingTypeServiceImpl implements BuildingTypeService {
    BuildingTypeRepository buildingTypeRepository;
    BuildingTypeMapper buildingTypeMapper;
    RequestParamUtils requestParamUtils;

    @Override
    @Transactional
    public BuildingTypeDto createBuildingType(BuildingTypeDto buildingTypeDto) {
        BuildingType buildingType = buildingTypeMapper.toEntity(buildingTypeDto);
        buildingType = buildingTypeRepository.save(buildingType);
        return buildingTypeMapper.toDto(buildingType);
    }

    @Override
    @SneakyThrows
    @Transactional
    public BuildingTypeDto updateBuildingType(Long buildingTypeId, BuildingTypeDto buildingTypeDto) {
        BuildingType buildingType = buildingTypeRepository.findById(buildingTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Building type not found"));
        buildingTypeMapper.partialUpdate(buildingType, buildingTypeDto);
        return buildingTypeMapper.toDto(buildingTypeRepository.save(buildingType));
    }

    @Override
    @SneakyThrows
    public void deleteBuildingType(Long id) {
        BuildingType buildingType = buildingTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Building type not found"));
        buildingTypeRepository.delete(buildingType);
    }

    @Override
    public List<BuildingTypeDto> getAllBuildingTypes() {
        return buildingTypeMapper.toDtoList(buildingTypeRepository.findAll());
    }


    @Override
    public Page<BuildingTypeDto> getBuildingTypes(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<BuildingType> buildingTypePage = buildingTypeRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(buildingTypePage.getTotalPages())
                .total(buildingTypePage.getTotalElements())
                .build();
        return Page.<BuildingTypeDto>builder()
                .meta(meta)
                .content(buildingTypePage.getContent().stream()
                        .map(buildingTypeMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
    }

}


