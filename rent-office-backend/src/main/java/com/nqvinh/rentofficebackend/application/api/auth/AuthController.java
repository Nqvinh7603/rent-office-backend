package com.nqvinh.rentofficebackend.application.api.auth;

import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.AuthRequestDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ForgotPasswordRequest;
import com.nqvinh.rentofficebackend.domain.auth.dto.response.AuthResponseDto;
import com.nqvinh.rentofficebackend.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(UrlConstant.AUTH)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping(UrlConstant.LOGIN)
    public ApiResponse<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequest,
                                              HttpServletResponse response) throws ResourceNotFoundException {
        return ApiResponse.<AuthResponseDto>builder()
                .status(HttpStatus.OK.value())
                .message("Login successful")
                .payload(authService.login(authRequest, response))
                .build();
    }

    @PostMapping(UrlConstant.REFRESH_TOKEN)
    public ApiResponse<AuthResponseDto> refreshAccessToken(@CookieValue("refresh_token") String refreshToken) throws ResourceNotFoundException {
        return ApiResponse.<AuthResponseDto>builder()
                .status(HttpStatus.OK.value())
                .payload(authService.refreshAccessToken(refreshToken))
                .build();
    }

    @PostMapping(UrlConstant.LOGOUT)
    public ApiResponse<Void> logout(HttpServletResponse response) throws ResourceNotFoundException {
        authService.logout(response);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Logout successful")
                .build();
    }


    @GetMapping("/failure")
    public ApiResponse<String> loginFailure() {
        return ApiResponse.<String>builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .payload("Login failed")
                .build();
    }

    @PostMapping(UrlConstant.FORGOT_PASSWORD)
    public ApiResponse<String> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
            authService.forgotPassword(forgotPasswordRequest);
            return ApiResponse.<String>builder()
                    .status(HttpStatus.OK.value())
                    .payload("Password reset link has been sent to your email.")
                    .build();
    }

}
