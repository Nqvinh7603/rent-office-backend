package com.nqvinh.rentofficebackend.domain.auth.mapper;

import com.nqvinh.rentofficebackend.domain.auth.dto.PermissionDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface PermissionMapper {
    PermissionDto toPermissionDTO(Permission permission);
    Permission toPermission(PermissionDto permissionDTO);
    void updatePermissionFromDTO(@MappingTarget Permission permission, PermissionDto permissionDTO);
}
