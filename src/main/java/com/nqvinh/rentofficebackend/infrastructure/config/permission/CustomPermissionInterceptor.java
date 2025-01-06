package com.nqvinh.rentofficebackend.infrastructure.config.permission;

import com.nqvinh.rentofficebackend.domain.auth.entity.Permission;
import com.nqvinh.rentofficebackend.domain.auth.entity.Role;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.infrastructure.audit.AuditAwareImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomPermissionInterceptor implements HandlerInterceptor {
    private final AuditAwareImpl auditAware;
    private final UserRepository userRepository;

    public boolean preHandle(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull Object handler) throws AccessDeniedException {

    String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
    String httpMethod = request.getMethod();
    logRequestDetails(path, httpMethod);

    String email = auditAware.getCurrentAuditor().orElse("");
    if (!email.isEmpty()) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            checkPermissions(user.orElse(null), path, httpMethod);
        }
    }

    return true;
}

    private void logRequestDetails(String path,  String httpMethod) {
        log.info("[PATH={}, METHOD={}]", path, httpMethod);
    }

    private void checkPermissions(User user, String path, String httpMethod) throws AccessDeniedException {
        List<Role> roles = user.getRoles();
        boolean isAllow = roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .anyMatch(permission -> permission.getApiPath().equals(path) && permission.getMethod().equals(httpMethod));

        if (!isAllow) {
            throw new AccessDeniedException("You do not have permission to access endpoint: " + path);
        }
    }
}
