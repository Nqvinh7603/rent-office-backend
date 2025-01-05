package com.nqvinh.rentofficebackend.application.api;
import com.nqvinh.rentofficebackend.application.dto.response.ApiResponse;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.AuthRequestDto;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public ApiResponse<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequest,
                                              HttpServletResponse response) throws ResourceNotFoundException {
        return ApiResponse.<AuthResponseDto>builder()
                .status(HttpStatus.OK.value())
                .payload(authService.login(authRequest, response))
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<AuthResponseDto> refreshAccessToken(@CookieValue("refresh_token") String refreshToken) throws ResourceNotFoundException {
        return ApiResponse.<AuthResponseDto>builder()
                .status(HttpStatus.OK.value())
                .payload(authService.refreshAccessToken(refreshToken))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) throws ResourceNotFoundException {
        authService.logout(response);
        return ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .build();
    }

//    @GetMapping("/success")
//    public ApiResponse<UserDTO> loginSuccess(@AuthenticationPrincipal OidcUser oidcUser) {
//        UserDTO userDTO = authService.processOAuthPostLogin(oidcUser);
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
