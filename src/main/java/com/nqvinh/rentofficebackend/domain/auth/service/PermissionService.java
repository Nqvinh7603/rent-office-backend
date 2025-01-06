package com.nqvinh.rentofficebackend.domain.auth.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.PermissionDto;

import java.util.List;
import java.util.Map;

public interface PermissionService {
    Page<PermissionDto> getPermissions(Map<String, String> params);
    PermissionDto createPermission(PermissionDto permissionDto) throws ResourceNotFoundException;
    void deletePermission(Long id) throws ResourceNotFoundException;
    PermissionDto updatePermission(Long id, PermissionDto permissionDto) throws ResourceNotFoundException;
    List<PermissionDto> getAllPermissions();
}
