package com.nqvinh.rentofficebackend.application.api.auth;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.enums.MessageEnums;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ChangePasswordReq;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(UrlConstant.USERS)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping
    public ApiResponse<Page<UserDto>> getUsers(@RequestParam Map<String, String> params) {
        return ApiResponse.<Page<UserDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Users"))
                .payload(userService.getUsers(params))
                .build();
    }

    @PostMapping
    @SneakyThrows
    public ApiResponse<UserDto> createUser(@Valid @RequestPart("user") UserDto userDto, @RequestParam(value = "user_img", required = false) MultipartFile userImg) {
        return ApiResponse.<UserDto>builder()
                .status(HttpStatus.CREATED.value())
                .message(MessageEnums.CREATED_SUCCESS.getMessage("User"))
                .payload(userService.createUser(userDto, userImg))
                .build();
    }

    @PutMapping(UrlConstant.UPDATE_USER)
    public ApiResponse<UserDto> updateUser(@PathVariable UUID id, @RequestPart("user") UserDto userDto, @RequestParam(value = "user_img", required = false) MultipartFile userImg) throws ResourceNotFoundException {
        return ApiResponse.<UserDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("User"))
                .payload(userService.updateUser(id, userDto, userImg))
                .build();
    }

    @DeleteMapping(UrlConstant.DELETE_USER)
    public ApiResponse<Void> deleteUser(@PathVariable UUID id) throws ResourceNotFoundException {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.DELETED_SUCCESS.getMessage("User"))
                .build();
    }

    @GetMapping(UrlConstant.LOGGED_IN_USER)
    public ApiResponse<UserDto> getLoggedInUser() throws ResourceNotFoundException {
        return ApiResponse.<UserDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("User"))
                .payload(userService.getLoggedInUser())
                .build();
    }

    @GetMapping(UrlConstant.GET_USER_BY_ID)
    public ApiResponse<UserDto> getUserById(@PathVariable UUID id) throws ResourceNotFoundException {
        return ApiResponse.<UserDto>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("User"))
                .payload(userService.getUserById(id))
                .build();
    }

    @PutMapping(UrlConstant.CHANGE_PASSWORD)
    public ApiResponse<Void> changePassword(@Valid @RequestBody ChangePasswordReq changePasswordReq) {
        userService.changePassword(changePasswordReq);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.UPDATED_SUCCESS.getMessage("Password"))
                .build();
    }

    @GetMapping(UrlConstant.GET_ALL_STAFF)
    public ApiResponse<List<UserDto>> loadStaff() {
        return ApiResponse.<List<UserDto>>builder()
                .status(HttpStatus.OK.value())
                .message(MessageEnums.FETCHED_SUCCESS.getMessage("Staff"))
                .payload(userService.loadStaff())
                .build();
    }
}
