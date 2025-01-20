package com.nqvinh.rentofficebackend.domain.auth.mapper;

import com.nqvinh.rentofficebackend.domain.auth.dto.RoleDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.Role;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses= {RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDto toUserDto(User user);

    User toUser(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", expression = "java(updateRole(user, userDto.getRole()))")
    void updateUserFromDto(@MappingTarget User user, UserDto userDto);

    default Role updateRole(User user, RoleDto roleDto) {
        if (roleDto == null || (user.getRole() != null && user.getRole().getRoleId().equals(roleDto.getRoleId()))) {
            return user.getRole();
        }
        Role newRole = new Role();
        newRole.setRoleId(roleDto.getRoleId());
        newRole.setRoleName(roleDto.getRoleName());
        newRole.setDescription(roleDto.getDescription());
        newRole.setActive(roleDto.isActive());
        return newRole;
    }

}
