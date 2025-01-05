package com.nqvinh.rentofficebackend.application.api;

import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
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
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    public ApiResponse<RoleDto> createRole(@Valid @RequestBody RoleDto roleRequestDTO) {
        return ApiResponse.<RoleDto>builder()
                .status(HttpStatus.CREATED.value())
                .payload(roleService.createRole(roleRequestDTO))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<RoleDto>> getRoles(@RequestParam Map<String,  String> params) {
        return ApiResponse.<Page<RoleDto>>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.getRoles(params))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoleDto> getRoleById(@PathVariable Long id) throws ResourceNotFoundException {
        return ApiResponse.<RoleDto>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.getRoleById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) throws ResourceNotFoundException {
        roleService.deleteRole(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleDto> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDto roleRequestDTO) throws ResourceNotFoundException {
        return ApiResponse.<RoleDto>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.updateRole(id, roleRequestDTO))
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<RoleDto>> getAllRoles() {
        return ApiResponse.<List<RoleDto>>builder()
                .status(HttpStatus.OK.value())
                .payload(roleService.getAllRoles())
                .build();
    }


}
