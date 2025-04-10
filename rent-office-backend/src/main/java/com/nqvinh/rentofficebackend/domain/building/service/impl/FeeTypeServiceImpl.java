/*******************************************************************************
 * Class        ：FeeTypeServiceImpl
 * Created date ：2025/03/12
 * Lasted date  ：2025/03/12
 * Author       ：vinhNQ2
 * Change log   ：2025/03/12：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.building.dto.FeeTypeDto;
import com.nqvinh.rentofficebackend.domain.building.entity.FeeType;
import com.nqvinh.rentofficebackend.domain.building.mapper.FeeTypeMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.FeeTypeRepository;
import com.nqvinh.rentofficebackend.domain.building.service.FeeTypeService;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * FeeTypeServiceImpl
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FeeTypeServiceImpl implements FeeTypeService {
//    BuildingTypeRepository buildingTypeRepository;
//    BuildingTypeMapper buildingTypeMapper;
//    PaginationUtils paginationUtils;
//
//    @Override
//    @Transactional
//    public BuildingTypeDto createBuildingType(BuildingTypeDto buildingTypeDto) {
//        BuildingType buildingType = buildingTypeMapper.toEntity(buildingTypeDto);
//        buildingType = buildingTypeRepository.save(buildingType);
//        return buildingTypeMapper.toDto(buildingType);
//    }
//
//    @Override
//    @SneakyThrows
//    @Transactional
//    public BuildingTypeDto updateBuildingType(Long buildingTypeId, BuildingTypeDto buildingTypeDto) {
//        BuildingType buildingType = buildingTypeRepository.findById(buildingTypeId)
//                .orElseThrow(() -> new ResourceNotFoundException("Building type not found"));
//        buildingTypeMapper.partialUpdate(buildingType, buildingTypeDto);
//        return buildingTypeMapper.toDto(buildingTypeRepository.save(buildingType));
//    }
//
//    @Override
//    @SneakyThrows
//    public void deleteBuildingType(Long id) {
//        BuildingType buildingType = buildingTypeRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Building type not found"));
//        buildingTypeRepository.delete(buildingType);
//    }
//
//    @Override
//    public List<BuildingTypeDto> getAllBuildingTypes() {
//        return buildingTypeMapper.toDtoList(buildingTypeRepository.findAll());
//    }
//
//    @Override
//    public Page<BuildingTypeDto> getBuildingTypes(Map<String, String> params) {
//        Pageable pageable = paginationUtils.buildPageable(params);
//        org.springframework.data.domain.Page<BuildingType> buildingTypePage = buildingTypeRepository.findAll(pageable);
//        Meta meta = paginationUtils.buildMeta(buildingTypePage, pageable);
//        return paginationUtils.mapPage(buildingTypePage, meta, buildingTypeMapper::toDto);
//    }

    FeeTypeRepository feeTypeRepository;
    FeeTypeMapper feeTypeMapper;
    PaginationUtils paginationUtils;


    @Override
    @Transactional
    public FeeTypeDto createFeeType(FeeTypeDto feeTypeDto) {
        FeeType feeType = feeTypeMapper.toEntity(feeTypeDto);
        feeType = feeTypeRepository.save(feeType);
        return feeTypeMapper.toDto(feeTypeRepository.save(feeType));
    }

    @Override
    @SneakyThrows
    @Transactional
    public FeeTypeDto updateFeeType(Long feeTypeId, FeeTypeDto feeTypeDto) {
        FeeType feeType = feeTypeRepository.findById(feeTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fee type not found"));
        feeTypeMapper.partialUpdate(feeType, feeTypeDto);
        return feeTypeMapper.toDto(feeTypeRepository.save(feeType));
    }

    @Override
    @SneakyThrows
    public void deleteFeeType(Long id) {
        FeeType feeType = feeTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fee type not found"));
        feeTypeRepository.delete(feeType);
    }

    @Override
    public List<FeeTypeDto> getAllFeeTypes() {
        return feeTypeMapper.toDtoList(feeTypeRepository.findAll());
    }

    @Override
    public Page<FeeTypeDto> getFeeTypes(Map<String, String> params) {
        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<FeeType> feeTypePage = feeTypeRepository.findAll(pageable);
        Meta meta = paginationUtils.buildMeta(feeTypePage, pageable);
        return paginationUtils.mapPage(feeTypePage, meta, feeTypeMapper::toDto);
    }
}