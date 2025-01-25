package com.nqvinh.rentofficebackend.application.api.auth;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.RoleDto;
import com.nqvinh.rentofficebackend.domain.auth.service.RoleService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlConstant.ROLES)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleDto> createRole(@Valid @RequestBody RoleDto roleRequestDTO) {
        return ApiResponse.<RoleDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Role"))
                .payload(roleService.createRole(roleRequestDTO))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<RoleDto>> getRoles(@RequestParam Map<String,  String> params) {
        return ApiResponse.<Page<RoleDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Roles"))
                .payload(roleService.getRoles(params))
                .build();
    }

    @GetMapping(UrlConstant.GET_ROLE_BY_ID)
    public ApiResponse<RoleDto> getRoleById(@PathVariable Long id) throws ResourceNotFoundException {
        return ApiResponse.<RoleDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Role"))
                .payload(roleService.getRoleById(id))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_ROLE)
    public ApiResponse<Void> deleteRole(@PathVariable Long id) throws ResourceNotFoundException {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Role"))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_ROLE)
    public ApiResponse<RoleDto> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDto roleRequestDTO) throws ResourceNotFoundException {
        return ApiResponse.<RoleDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Role"))
                .payload(roleService.updateRole(id, roleRequestDTO))
                .build();
    }

    @GetMapping(UrlConstant.GET_ALL_ROLE)
    public ApiResponse<List<RoleDto>> getAllRoles() {
        return ApiResponse.<List<RoleDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Roles"))
                .payload(roleService.getAllRoles())
                .build();
    }
}
