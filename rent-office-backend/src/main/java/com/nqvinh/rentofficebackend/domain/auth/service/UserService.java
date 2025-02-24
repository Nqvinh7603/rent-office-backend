package com.nqvinh.rentofficebackend.domain.auth.service;

import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ChangePasswordReq;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {
    UserDto createUser(UserDto userDto, MultipartFile userImg) throws NoSuchAlgorithmException, InvalidKeySpecException;

    Page<UserDto> getUsers(Map<String, String> params);

    void deleteUser(UUID id) throws ResourceNotFoundException;

    UserDto updateUser(UUID id, UserDto userDto, MultipartFile userImg) throws ResourceNotFoundException;

    UserDto getLoggedInUser() throws ResourceNotFoundException;

    UserDto getUserById(UUID id) throws ResourceNotFoundException;

    void changePassword(ChangePasswordReq changePasswordReq);

    List<User> getUsersByIdIn(List<UserDto> userIds);

    List<UserDto> loadStaff();

    List<UserDto> getAllAdminsAndManagers();

}
