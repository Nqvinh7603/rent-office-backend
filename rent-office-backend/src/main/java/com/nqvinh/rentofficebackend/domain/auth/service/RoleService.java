package com.nqvinh.rentofficebackend.domain.auth.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.RoleDto;

import java.util.List;
import java.util.Map;

public interface RoleService {
    RoleDto createRole(RoleDto roleDTO);

    Page<RoleDto> getRoles(Map<String, String> params);

    RoleDto getRoleById(Long id) throws ResourceNotFoundException;

    void deleteRole(Long id) throws ResourceNotFoundException;

    RoleDto updateRole(Long id, RoleDto roleDTO) throws ResourceNotFoundException;

    List<RoleDto> getAllRoles();
}
