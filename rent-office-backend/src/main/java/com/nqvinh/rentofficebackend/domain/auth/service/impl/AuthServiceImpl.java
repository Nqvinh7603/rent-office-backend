package com.nqvinh.rentofficebackend.domain.auth.service.impl;

import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.AuthRequestDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ForgotPasswordRequest;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ResetPasswordRequest;
import com.nqvinh.rentofficebackend.domain.auth.dto.response.AuthResponseDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.AuthService;
import com.nqvinh.rentofficebackend.domain.common.constant.MailStatus;
import com.nqvinh.rentofficebackend.domain.common.constant.MailType;
import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;
import com.nqvinh.rentofficebackend.domain.common.service.MailProducer;
import com.nqvinh.rentofficebackend.infrastructure.audit.AuditAwareImpl;
import com.nqvinh.rentofficebackend.domain.common.service.RedisService;
import com.nqvinh.rentofficebackend.infrastructure.utils.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    JwtDecoder jwtDecoder;
    AuditAwareImpl auditAware;
    JwtUtils jwtUtils;
    MailProducer mailProducer;
    RedisService redisService;
    PasswordEncoder passwordEncoder;

    @Override
    @SneakyThrows
    public AuthResponseDto login(AuthRequestDto authRequest, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        // Store refresh token in http only cookie
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setAttribute("SameSite", "Strict");
        refreshTokenCookie.setMaxAge(authRequest.isRememberMe() ? 30 * 24 * 60 * 60 : -1); // 30 days if rememberMe is true, otherwise 0 days

        response.addCookie(refreshTokenCookie);
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    @SneakyThrows
    public AuthResponseDto refreshAccessToken(String refreshToken) {
        Jwt jwtRefreshToken = jwtDecoder.decode(refreshToken);
        String username = jwtUtils.getUsername(jwtRefreshToken);
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return AuthResponseDto.builder()
                .accessToken(jwtUtils.generateAccessToken(user))
                .build();
    }

    @Override
    @SneakyThrows
    public void logout(HttpServletResponse httpServletResponse)  {
        String email = auditAware.getCurrentAuditor().orElse("");

        if (email.isEmpty()) {
            throw new ResourceNotFoundException("Access Token not valid");
        }
        SecurityContextHolder.clearContext();
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);

        httpServletResponse.addCookie(refreshTokenCookie);

    }

    @Override
    @SneakyThrows
    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        User user = userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.isActive()) throw new ResourceNotFoundException("User is not active");
        redisService.delete("forgot-password:" + user.getEmail());

        String token = jwtUtils.generateResetPasswordToken(user);
        redisService.set("forgot-password:" + user.getEmail(), token, 30 * 60 * 1000); // 30 minutes
        String resetPasswordLink = forgotPasswordRequest.getSiteUrl() + "/reset-password?token=" + token;

        Map<String, Object> context = Map.of(
                "firstName", user.getFirstName(),
                "lastName", user.getLastName(),
                "resetPasswordLink", resetPasswordLink
        );

        var mailResetPassword = MailEvent.builder()
                .toAddress(user.getEmail())
                .subject("Đặt lại mật khẩu")
                .templateName("password-reset-template")
                .context(context)
                .status(MailStatus.INIT.getStatus())
                .code(MessageCode.MAIL_RESET_PASSWORD.getCode())
                .type(MailType.RESET_PASSWORD.getType())
                .build();

        mailProducer.send(mailResetPassword);
    }

    @Override
    @SneakyThrows
    @Transactional
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        String email = jwtUtils.getUsername(jwtDecoder.decode(resetPasswordRequest.getToken()));
        String token = redisService.get("forgot-password:" + email).toString();
        if (!resetPasswordRequest.getToken().equals(token)) {
            throw new ResourceNotFoundException("Token is invalid");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!user.isActive()) {
            throw new ResourceNotFoundException("User account is not active");
        }
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);
        redisService.delete("forgot-password:" + email);
    }

}
