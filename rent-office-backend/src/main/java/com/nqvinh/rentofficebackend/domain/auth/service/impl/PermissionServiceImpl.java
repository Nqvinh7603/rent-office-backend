package com.nqvinh.rentofficebackend.domain.auth.service.impl;

import com.nqvinh.rentofficebackend.application.dto.request.SearchCriteria;
import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.PermissionDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.Permission;
import com.nqvinh.rentofficebackend.domain.auth.mapper.PermissionMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.PermissionRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.PermissionService;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.RequestParamUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {
    PermissionRepository permissionRepository;
    RequestParamUtils requestParamUtils;
    PermissionMapper permissionMapper;
    PaginationUtils paginationUtils;

    @Override
    public Page<PermissionDto> getPermissions(Map<String, String> params) {
        Specification<Permission> spec = getPermissionSpec(params);
        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<Permission> permissionPage = permissionRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(permissionPage, pageable);
        return paginationUtils.mapPage(permissionPage, meta, permissionMapper::toDto);
    }

    private Specification<Permission> getPermissionSpec(Map<String, String> params) {
        Specification<Permission> spec = Specification.where(null);
        List<SearchCriteria> methodCriteria = requestParamUtils.getSearchCriteria(params, "method");
        List<SearchCriteria> moduleCriteria = requestParamUtils.getSearchCriteria(params, "module");
        Specification<Permission> methodSpec = Specification.where(null);
        for (SearchCriteria criteria : methodCriteria) {
            methodSpec = methodSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("method"), criteria.getValue())));
        }
        Specification<Permission> moduleSpec = Specification.where(null);
        for (SearchCriteria criteria : moduleCriteria) {
            moduleSpec = moduleSpec.or(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("module"), criteria.getValue())));
        }
        spec = spec.and(methodSpec).and(moduleSpec);
        return spec;
    }

    @Transactional
    @Override
    public PermissionDto createPermission(PermissionDto permissionDto) throws ResourceNotFoundException {
        if (permissionRepository.existsByModuleAndApiPathAndMethod(permissionDto.getModule(), permissionDto.getApiPath(), permissionDto.getMethod())) {
            throw new ResourceNotFoundException("Permission already exists");
        }
        Permission newPermission = permissionRepository.save(permissionMapper.toEntity(permissionDto));
        return permissionMapper.toDto(newPermission);
    }

    @Override
    public void deletePermission(Long id) throws ResourceNotFoundException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        permissionRepository.delete(permission);
    }

    @Transactional
    @Override
    public PermissionDto updatePermission(Long id, PermissionDto permissionDto) throws ResourceNotFoundException {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        permissionMapper.partialUpdate(permission, permissionDto);
        return permissionMapper.toDto(permissionRepository.save(permission));
    }

    @Override
    public List<PermissionDto> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toDto)
                .collect(Collectors.toList());
    }
}
