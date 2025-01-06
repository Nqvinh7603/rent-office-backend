package com.nqvinh.rentofficebackend.application.api.auth;
import com.nqvinh.rentofficebackend.application.constant.UrlConstant;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.AuthRequestDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.response.AuthResponseDto;
import com.nqvinh.rentofficebackend.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@RestController
@RequestMapping(UrlConstant.AUTH)
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping(UrlConstant.LOGIN)
    public ApiResponse<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequest,
                                              HttpServletResponse response) throws ResourceNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
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

//    @GetMapping("/success")
//    public ApiResponse<UserDto> loginSuccess(@AuthenticationPrincipal OidcUser oidcUser) {
//        UserDto userDTO = authService.processOAuthPostLogin(oidcUser);
//        return ApiResponse.<UserDTO>builder()
//                .status(HttpStatus.OK.value())
//                .payload(userDTO)
//                .build();
//    }



    @GetMapping("/failure")
    public ApiResponse<String> loginFailure() {
        return ApiResponse.<String>builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .payload("Login failed")
                .build();
    }

//    @PostMapping("/forgot-password")
//    public ApiResponse<String> forgotPassword(@RequestParam String email, @RequestParam String siteUrl) {
//        try {
//            authService.forgotPassword(email, siteUrl);
//            return ApiResponse.<String>builder()
//                    .status(HttpStatus.OK.value())
//                    .payload("Password reset link has been sent to your email.")
//                    .build();
//        } catch (Exception e) {
//            return ApiResponse.<String>builder()
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                    .payload(e.getMessage())
//                    .build();
//        }
//    }
//
//    @PostMapping("/reset-password")
//    public ApiResponse<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
//        try {
//            authService.resetPassword(token, newPassword);
//            return ApiResponse.<String>builder()
//                    .status(HttpStatus.OK.value())
//                    .payload("Password has been reset successfully.")
//                    .build();
//        } catch (Exception e) {
//            return ApiResponse.<String>builder()
//                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                    .payload(e.getMessage())
//                    .build();
//        }
//    }
//
//
//    @GetMapping("/verify-reset-token")
//    public ApiResponse<Void> verifyResetToken(@RequestParam String token) throws ResourceNotFoundException {
//        authService.verifyResetToken(token);
//        return ApiResponse.<Void>builder()
//                .status(HttpStatus.OK.value())
//                .build();
//    }

}
