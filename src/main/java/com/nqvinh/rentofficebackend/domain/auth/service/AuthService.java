package com.nqvinh.rentofficebackend.domain.auth.service;

import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.AuthRequestDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.response.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
   // UserDTO register(UserDTO userDTO, String  siteUrl) throws ResourceNotFoundException, MessagingException, UnsupportedEncodingException;
    AuthResponseDto login(AuthRequestDto authRequest, HttpServletResponse response) throws ResourceNotFoundException;
    AuthResponseDto refreshAccessToken(String refreshToken) throws ResourceNotFoundException;
    void logout(HttpServletResponse httpServletResponse) throws ResourceNotFoundException;
    //UserDTO processOAuthPostLogin(OidcUser oidcUser);
   // UserDTO verifyUser(String token) throws ResourceNotFoundException;
    //void forgotPassword(String email, String siteUrl) throws ResourceNotFoundException, MessagingException, UnsupportedEncodingException;
//    void resetPassword(String token, String newPassword) throws ResourceNotFoundException;
//    void verifyResetToken(String token) throws ResourceNotFoundException;
}
