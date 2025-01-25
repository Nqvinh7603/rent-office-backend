package com.nqvinh.rentofficebackend.application.api.auth;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.PermissionDto;
import com.nqvinh.rentofficebackend.domain.auth.service.PermissionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(UrlConstant.PERMISSIONS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ApiResponse<Page<PermissionDto>> getPermissions(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<PermissionDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Permissions"))
                .payload(permissionService.getPermissions(params))
                .build();
    }

    @GetMapping(UrlConstant.GET_ALL_PERMISSION)
    public ApiResponse<List<PermissionDto>> getAllPermissions() {
        return ApiResponse.<List<PermissionDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Permissions"))
                .payload(permissionService.getAllPermissions())
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionDto> createPermission(@Valid @RequestBody PermissionDto permissionDto) throws ResourceNotFoundException {
        return ApiResponse.<PermissionDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("Permission"))
                .payload(permissionService.createPermission(permissionDto))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_PERMISSION)
    public ApiResponse<PermissionDto> updatePermission(@PathVariable Long id, @Valid @RequestBody PermissionDto permissionDto) throws ResourceNotFoundException {
        return ApiResponse.<PermissionDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Permission"))
                .payload(permissionService.updatePermission(id, permissionDto))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_PERMISSION)
    public ApiResponse<Void> deletePermission(@PathVariable Long id) throws ResourceNotFoundException {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("Permission"))
                .build();
    }
}
