package com.nqvinh.rentofficebackend.domain.auth.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.dto.request.ChangePasswordReq;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.RoleRepository;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.infrastructure.service.ImageService;
import com.nqvinh.rentofficebackend.infrastructure.utils.RequestParamUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    ImageService imageService;
    RoleRepository roleRepository;

    @Override
    @Transactional
    @SneakyThrows
    public UserDto createUser(UserDto userDto, MultipartFile userImg) {
        userDto.setAvatarUrl(imageService.handleImageUpload(userImg, userDto.getAvatarUrl()));
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.toDto(userRepository.save(user));
    }


    @Override
    public Page<UserDto> getUsers(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        Specification<User> spec = getUserSpec(params);
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, User.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<User> userPage = userRepository.findAll(spec, pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(userPage.getTotalPages())
                .total(userPage.getTotalElements())
                .build();
        return Page.<UserDto>builder()
                .meta(meta)
                .content(userPage.getContent().stream()
                        .map(userMapper::toDto)
                        .collect(Collectors.toList()))
                .build();
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
       userDto.setAvatarUrl(userImg == null ? "" : imageService.handleImageUpload(userImg, userDto.getAvatarUrl()));
        userMapper.partialUpdate(user, userDto);
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getLoggedInUser() throws ResourceNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return userMapper.toDto(user);
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
    @SneakyThrows
    @Transactional
    public void changePassword(UUID id, ChangePasswordReq changePasswordReq) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordReq.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (passwordEncoder.matches(changePasswordReq.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from the current password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordReq.getNewPassword()));
        userRepository.save(user);
    }
}
