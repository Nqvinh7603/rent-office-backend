package com.nqvinh.rentofficebackend.domain.auth.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.RoleDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.Role;
import com.nqvinh.rentofficebackend.domain.auth.mapper.RoleMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.RoleRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    @Override
    public RoleDto createRole(RoleDto roleDTO) {
        Role role = roleMapper.toRole(roleDTO);
        return roleMapper.toRoleDto(roleRepository.save(role));
    }

    @Override
    public Page<RoleDto> getRoles(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        org.springframework.data.domain.Page<Role> pageRole = roleRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(pageRole.getNumber() + 1)
                .pageSize(pageRole.getSize())
                .pages(pageRole.getTotalPages())
                .total(pageRole.getTotalElements())
                .build();

        return Page.<RoleDto>builder()
                .meta(meta)
                .content(pageRole.getContent().stream()
                        .map(roleMapper::toRoleDto)
                        .collect(Collectors.toList()))
                .build();
    }



    @Override
    public RoleDto getRoleById(Long id) throws ResourceNotFoundException {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleDto)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
    }

    @Override
    public void deleteRole(Long id) throws ResourceNotFoundException {
       Role role = roleRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
        roleRepository.deleteById(id);
    }

    @Override
    public RoleDto updateRole(Long id, RoleDto roleDTO) throws ResourceNotFoundException {
        Role roleToUpdate = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found role with ID " + id));
        roleMapper.updateRoleFromDTO(roleToUpdate, roleDTO);
        return roleMapper.toRoleDto(roleRepository.save(roleToUpdate));
    }

    @Override
    public List<RoleDto> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().map(roleMapper::toRoleDto).collect(Collectors.toList());
    }
}
