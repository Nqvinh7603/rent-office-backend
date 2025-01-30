package com.nqvinh.rentofficebackend.domain.auth.service;

import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.AuthRequestDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ForgotPasswordRequest;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ResetPasswordRequest;
import com.nqvinh.rentofficebackend.domain.auth.dto.response.AuthResponseDto;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponseDto login(AuthRequestDto authRequest, HttpServletResponse response) throws ResourceNotFoundException;
    AuthResponseDto refreshAccessToken(String refreshToken) throws ResourceNotFoundException;
    void logout(HttpServletResponse httpServletResponse) throws ResourceNotFoundException;
    void resetPassword(ResetPasswordRequest resetPasswordRequest);
    void forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
}
