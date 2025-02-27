package com.nqvinh.rentofficebackend.domain.auth.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.InvalidPasswordException;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ChangePasswordReq;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.domain.common.entity.Notification;
import com.nqvinh.rentofficebackend.domain.common.mapper.NotificationMapper;
import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    StringUtils stringUtils;
    ImageService imageService;
    PaginationUtils paginationUtils;
    NotificationMapper notificationMapper;

    @Override
    @Transactional
    @SneakyThrows
    public UserDto createUser(UserDto userDto, MultipartFile userImg) {
        userDto.setAvatarUrl(imageService.handleImageUpload(userImg, userDto.getAvatarUrl()).get());
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public Page<UserDto> getUsers(Map<String, String> params) {
        Specification<User> spec = getUserSpec(params);
        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<User> userPage = userRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(userPage, pageable);
        return paginationUtils.mapPage(userPage, meta, userMapper::toDto);
    }


    private Specification<User> getUserSpec(Map<String, String> params) {
        Specification<User> spec = Specification.where(null);

        if (params.containsKey("query")) {
            String searchValue = stringUtils.normalizeString(params.get("query").trim().toLowerCase());
            String likePattern = "%" + searchValue + "%";
            spec = spec.or((root, query, criteriaBuilder) -> {
                query.distinct(true);
                return criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("email"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(
                                        criteriaBuilder.concat(
                                                root.get("lastName"),
                                                criteriaBuilder.concat(" ", root.get("firstName"))
                                        )
                                )),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("phoneNumber"))),
                                likePattern
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("role").get("roleName"))),
                                likePattern
                        )
                );
            });
        }

        if (params.containsKey("active")) {
            boolean isActive = Boolean.parseBoolean(params.get("active"));
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), isActive));
        }

        if (params.containsKey("role")) {
            String role = params.get("role");
            spec = spec.and((root, query, cb) -> cb.equal(root.get("role").get("roleName"), role));
        }
        return spec;
    }


    @Override
    public void deleteUser(UUID id) throws ResourceNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }


    @Override
    @Transactional
    @SneakyThrows
    public UserDto updateUser(UUID id, UserDto userDto, MultipartFile userImg) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userDto.setAvatarUrl(userImg == null ? "" :
                imageService.handleImageUpload(userImg, userDto.getAvatarUrl()).get());
        userMapper.partialUpdate(user, userDto);
        return userMapper.toDto(userRepository.save(user));
    }


    @Override
    public UserDto getLoggedInUser() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            UserDto userDto = userMapper.toDto(user);
            userDto.setNotifications(user.getNotifications().stream()
                    .sorted(Comparator.comparing(Notification::isStatus).reversed()
                            .thenComparing(Notification::getCreatedAt).reversed())
                    .map(notificationMapper::toDto)
                    .collect(Collectors.toList()));
            return userDto;
        }
        return null;
    }

    @Override
    @SneakyThrows
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    @SneakyThrows
    public void changePassword(ChangePasswordReq changePasswordReq) {
        User loggedInUser = userRepository.findByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateChangePassword(changePasswordReq, loggedInUser);

        loggedInUser.setPassword(passwordEncoder.encode(changePasswordReq.getNewPassword()));
        userRepository.save(loggedInUser);
    }

    @Override
    public List<User> getUsersByIdIn(List<UserDto> userIds) {
        return userRepository.findByUserIdIn(userIds.stream().map(UserDto::getUserId).toList());
    }

    @Override
    public List<UserDto> loadStaff() {
        return userMapper.toDtoList(userRepository.findByActiveAndRole_RoleName(true, "EMPLOYEE"));
    }

    @Override
    public List<UserDto> getAllAdminsAndManagers() {
        List<User> adminsAndManagers = userRepository.findByActiveAndRole_RoleNameIn(true, List.of("ADMIN", "MANAGER"));
        return userMapper.toDtoList(adminsAndManagers);
    }

    public List<UserDto> getAllUserByCustomerId(Long customerId) {
        return userMapper.toDtoList(userRepository.findUsersByCustomerId(customerId));
    }

    private void validateChangePassword(ChangePasswordReq req, User user) {
        switch (req) {
            case ChangePasswordReq r when !passwordEncoder.matches(r.getCurrentPassword(), user.getPassword()) ->
                    throw new InvalidPasswordException("Current password is incorrect");
            case ChangePasswordReq r when !r.getNewPassword().equals(r.getConfirmPassword()) ->
                    throw new InvalidPasswordException("New password and confirm password do not match");
            case ChangePasswordReq r when passwordEncoder.matches(r.getNewPassword(), user.getPassword()) ->
                    throw new InvalidPasswordException("New password must be different from the current password");
            default -> {
            }
        }
    }
}
