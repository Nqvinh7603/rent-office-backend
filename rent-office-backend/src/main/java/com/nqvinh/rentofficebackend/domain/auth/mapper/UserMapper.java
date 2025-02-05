package com.nqvinh.rentofficebackend.domain.auth.mapper;

import com.nqvinh.rentofficebackend.domain.auth.dto.RoleDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.Role;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapper;
import com.nqvinh.rentofficebackend.domain.common.mapper.CommonMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class}, config = CommonMapperConfig.class)
public interface UserMapper extends CommonMapper<UserDto, User> {

    @Override
    @Mapping(target = "password", ignore = true)
    UserDto toDto(User user);

    @Override
    User toEntity(UserDto userDto);

    @Override
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", expression = "java(updateRole(entity, dto.getRole()))")
    void partialUpdate(@MappingTarget User entity, UserDto dto);

    default Role updateRole(User user, RoleDto roleDto) {
        if (roleDto == null || (user.getRole() != null && user.getRole().getRoleId().equals(roleDto.getRoleId()))) {
            return user.getRole();
        }
        Role newRole = new Role();
        newRole.setRoleId(roleDto.getRoleId());
        return newRole;
    }
}
