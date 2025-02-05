package com.nqvinh.rentofficebackend.domain.auth.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.RoleDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.Role;
import com.nqvinh.rentofficebackend.domain.auth.mapper.RoleMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.RoleRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.RoleService;
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
public class RoleServiceImpl implements RoleService {
    RoleMapper roleMapper;
    RoleRepository roleRepository;
    PaginationUtils paginationUtils;

    RequestParamUtils requestParamUtils;
    @Transactional
    @Override
    public RoleDto createRole(RoleDto roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    public Page<RoleDto> getRoles(Map<String, String> params) {
        Specification<Role> spec = getRoleSpec(params);
        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<Role> rolePage = roleRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(rolePage, pageable);
        return paginationUtils.mapPage(rolePage, meta, roleMapper::toDto);
    }

    private Specification<Role> getRoleSpec(Map<String, String> params) {
        Specification<Role> spec = Specification.where(null);

        if (params.containsKey("active")) {
            boolean isActive = Boolean.parseBoolean(params.get("active"));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), isActive));
        }
        return spec;
    }

    @Override
    public RoleDto getRoleById(Long id) throws ResourceNotFoundException {
        return roleRepository.findById(id)
                .map(roleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
    }

    @Override
    public void deleteRole(Long id) throws ResourceNotFoundException {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
        if (role.getUsers().isEmpty()) {
            roleRepository.delete(role);
        } else {
            throw new IllegalStateException("Cannot delete role with associated users");
        }
    }

    @Transactional
    @Override
    public RoleDto updateRole(Long id, RoleDto roleDTO) throws ResourceNotFoundException {
        Role roleToUpdate = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
        roleMapper.partialUpdate(roleToUpdate, roleDTO);
        return roleMapper.toDto(roleRepository.save(roleToUpdate));
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toDto).collect(Collectors.toList());
    }
}
