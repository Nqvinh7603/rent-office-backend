package com.nqvinh.rentofficebackend.domain.customer.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.domain.common.constant.MailStatus;
import com.nqvinh.rentofficebackend.domain.common.constant.MailType;
import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;
import com.nqvinh.rentofficebackend.domain.common.service.EmailProducer;
import com.nqvinh.rentofficebackend.domain.common.service.NotificationService;
import com.nqvinh.rentofficebackend.domain.common.service.RedisService;
import com.nqvinh.rentofficebackend.domain.customer.constant.ConsignmentStatus;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentStatusHistoryDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.response.CustomerResDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import com.nqvinh.rentofficebackend.domain.customer.entity.ConsignmentStatusHistory;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import com.nqvinh.rentofficebackend.domain.customer.mapper.ConsignmentMapper;
import com.nqvinh.rentofficebackend.domain.customer.mapper.response.CustomerResMapper;
import com.nqvinh.rentofficebackend.domain.customer.repository.ConsignmentRepository;
import com.nqvinh.rentofficebackend.domain.customer.repository.CustomerRepository;
import com.nqvinh.rentofficebackend.domain.customer.service.ConsignmentImageService;
import com.nqvinh.rentofficebackend.domain.customer.service.ConsignmentService;
import com.nqvinh.rentofficebackend.domain.customer.service.CustomerService;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsignmentServiceImpl implements ConsignmentService {

    CustomerRepository customerRepository;
    ConsignmentMapper consignmentMapper;
    PaginationUtils paginationUtils;
    StringUtils stringUtils;
    ConsignmentRepository consignmentRepository;
    ConsignmentImageService consignmentImageService;
    CustomerResMapper customerResMapper;
    UserService userService;
    UserMapper userMapper;
    NotificationService notificationService;
    CustomerService customerService;
    EmailProducer emailProducer;
    RedisService redisService;

    @Override
    @SneakyThrows
    @Transactional
    public CustomerResDto createConsignment(CustomerReqDto customerReqDto, List<MultipartFile> consignmentImages) {
        Customer customer = customerService.findOrCreateCustomer(customerReqDto);
        List<String> uploadedUrls = consignmentImageService.uploadConsignmentImages(customerReqDto, consignmentImages);
        List<Consignment> newConsignments = consignmentImageService.convertConsignmentDtoToEntities(customerReqDto, uploadedUrls, customer);
        customer.getConsignments().addAll(newConsignments);

        sendMailNewConsignment(customer, newConsignments);
        Customer savedCustomer = customerRepository.save(customer);

        List<UserDto> adminsAndManagers = userService.getAllAdminsAndManagers();
        adminsAndManagers.forEach(adminOrManager -> notificationService.createConsignmentNotification(adminOrManager, savedCustomer));
        return customerResMapper.toDto(savedCustomer);
    }

    private void sendMailNewConsignment(Customer customer, List<Consignment> newConsignments) {
        var mailConfirmedConsignment = MailEvent.builder()
                .toAddress(customer.getEmail())
                .subject("Xác nhận tài sản ký gửi")
                .templateName("pending-consignment-template")
                .context(Map.of(
                        "customerName", customer.getCustomerName(),
                        "price", newConsignments.getFirst().getPrice(),
                        "city", newConsignments.getFirst().getCity(),
                        "district", newConsignments.getFirst().getDistrict(),
                        "ward", newConsignments.getFirst().getWard(),
                        "street", newConsignments.getFirst().getStreet(),
                        "description", newConsignments.getFirst().getDescription()
                ))
                .status(MailStatus.INIT.getStatus())
                .code(MessageCode.MAIL_PENDING_CONSIGNMENT.getCode())
                .type(MailType.PENDING_CONSIGNMENT.getType())
                .build();
        emailProducer.sendMailNewConsignment(mailConfirmedConsignment);
    }

    @Override
    @SneakyThrows
    public Page<ConsignmentDto> getCustomerConsignments(Map<String, String> params) {
        UserDto currentUser = userService.getLoggedInUser();
        Specification<Consignment> spec = getCustomerSpec(params);

        if (!isAdmin(currentUser)) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, Consignment> customerJoin = root.join("customer", JoinType.INNER);
                Join<Customer, User> userJoin = customerJoin.join("users", JoinType.INNER);
                return criteriaBuilder.equal(userJoin.get("userId"), currentUser.getUserId());
            });
        }

        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<Consignment> consignmentPage = consignmentRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(consignmentPage, pageable);
        return paginationUtils.mapPage(consignmentPage, meta, consignmentMapper::toDto);
    }

    private boolean isAdmin(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        String roleName = user.getRole().getRoleName();
        return roleName.equals("ADMIN") || roleName.equals("MANAGER");
    }

    @Override
    @SneakyThrows
    public void deleteConsignment(Long consignmentId) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Consignment not found"));
        consignmentRepository.delete(consignment);
    }

    @Override
    @Transactional
    @SneakyThrows
    public ConsignmentDto updateConsignment(Long consignmentId, ConsignmentDto consignmentDto, List<MultipartFile> consignmentImages, List<String> deletedImages) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Consignment not found"));
        String token = UUID.randomUUID().toString();
        List<ConsignmentStatusHistory> existingStatusHistories = consignment.getConsignmentStatusHistories();

        String additionalInfoLink = "http://localhost:5173/ky-gui/" + consignmentId + "?token=" + token;

        if (consignmentDto.getConsignmentStatusHistories().getLast().getStatus().equals(ConsignmentStatus.INCOMPLETE.toString())) {
            redisService.set("additional-info:" + consignmentId, token);

            var mailIncompleteConsignment = MailEvent.builder()
                    .toAddress(consignment.getCustomer().getEmail())
                    .subject("Cập nhật thông tin tài sản ký gửi")
                    .templateName("incomplete-consignment-template")
                    .context(Map.of(
                            "customerName", consignmentDto.getCustomer().getCustomerName(),
                            "price", consignmentDto.getPrice(),
                            "city", consignmentDto.getCity(),
                            "district", consignmentDto.getDistrict(),
                            "ward", consignmentDto.getWard(),
                            "street", consignmentDto.getStreet(),
                            "description", consignmentDto.getDescription(),
                            "additionalInfo", consignmentDto.getConsignmentStatusHistories().getFirst().getNote(),
                            "additionalInfoLink", additionalInfoLink
                    ))
                    .status(MailStatus.INIT.getStatus())
                    .code(MessageCode.MAIL_INCOMPLETE_CONSIGNMENT.getCode())
                    .type(MailType.INCOMPLETE_CONSIGNMENT.getType())
                    .build();
            emailProducer.sendMailIncompleteConsignment(mailIncompleteConsignment);
        } else if (consignmentDto.getConsignmentStatusHistories().getLast().getStatus().equals(ConsignmentStatus.ADDITIONAL_INFO.toString())) {
            List<UserDto> users = userService.getAllUserByCustomerId(consignmentDto.getCustomer().getCustomerId());
            users.forEach(user ->
                    notificationService.updateInfoConsignmentNotification(user, consignmentDto));
            redisService.delete("additional-info:" + consignmentId);
        } else if (consignmentDto.getConsignmentStatusHistories().getLast().toString().equals(ConsignmentStatus.CANCELLED.toString())) {

            var mailCancelledConsignment = MailEvent.builder()
                    .toAddress(consignmentDto.getCustomer().getEmail())
                    .subject("Từ chối tài sản ký gửi")
                    .templateName("cancelled-consignment-template")
                    .context(Map.of(
                            "customerName", consignmentDto.getCustomer().getCustomerName(),
                            "price", consignmentDto.getPrice(),
                            "city", consignmentDto.getCity(),
                            "district", consignmentDto.getDistrict(),
                            "ward", consignmentDto.getWard(),
                            "street", consignmentDto.getStreet(),
                            "rejectedReason", consignmentDto.getConsignmentStatusHistories().getFirst().getNote()
                    ))
                    .status(MailStatus.INIT.getStatus())
                    .code(MessageCode.MAIL_CANCELLED_CONSIGNMENT.getCode())
                    .type(MailType.CANCELLED_CONSIGNMENT.getType())
                    .build();
            emailProducer.sendMailCancelledConsignment(mailCancelledConsignment);
        }

        if (consignmentImages != null && !consignmentImages.isEmpty()) {
            consignmentImageService.updateConsignmentImages(consignment, consignmentImages);
        }
        if (deletedImages != null && !deletedImages.isEmpty()) {
            consignmentImageService.deleteConsignmentImages(consignment, deletedImages);
        }

        consignmentDto.getConsignmentStatusHistories().stream()
                .filter(statusDto -> statusDto.getConsignmentStatusHistoryId() == null)
                .map(statusDto -> ConsignmentStatusHistory.builder()
                        .status(ConsignmentStatus.valueOf(statusDto.getStatus()))
                        .note(statusDto.getNote())
                        .consignment(consignment)
                        .build())
                .forEach(existingStatusHistories::add);
        consignment.setConsignmentStatusHistories(existingStatusHistories);

        consignmentMapper.partialUpdate(consignment, consignmentDto);
        consignment.getConsignmentImages().forEach(image -> image.setConsignment(consignment));
        consignment.getConsignmentStatusHistories().forEach(status -> status.setConsignment(consignment));
        Consignment savedConsignment = consignmentRepository.save(consignment);
        return consignmentMapper.toDto(savedConsignment);
    }


    @Override
    @SneakyThrows
    public ConsignmentDto getConsignmentById(Long consignmentId) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Consignment not found"));
        return consignmentMapper.toDto(consignment);
    }

    @Override
    @SneakyThrows
    public void verifyTokenConsignment(String consignmentId, String token) {
        String tokenInRedis = redisService.get("additional-info:" + consignmentId).toString();
        if (!token.equals(tokenInRedis)) {
            throw new ResourceNotFoundException("Token is invalid");
        }
    }


    private Specification<Consignment> getCustomerSpec(Map<String, String> params) {
        Specification<Consignment> spec = Specification.where(null);

        if (params.containsKey("email")) {
            String email = stringUtils.normalizeString(params.get("email").trim().toLowerCase());
            String likePattern = "%" + email + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("customer").get("email"))), likePattern)
            );
        }

        if (params.containsKey("customerName")) {
            String customerName = stringUtils.normalizeString(params.get("customerName").trim().toLowerCase());
            String likePattern = "%" + customerName + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("customer").get("customerName"))), likePattern)
            );
        }

        if (params.containsKey("phoneNumber")) {
            String phoneNumber = stringUtils.normalizeString(params.get("phoneNumber").trim().toLowerCase());
            spec = spec.and((root, query, cb) -> {
                Join<Customer, Consignment> customerJoin = root.join("customer", JoinType.INNER);
                return cb.equal(customerJoin.get("phoneNumber"), phoneNumber);
            });
        }

        if (params.containsKey("buildingType")) {
            String buildingType = stringUtils.normalizeString(params.get("buildingType").trim().toLowerCase());
            String likePattern = "%" + buildingType + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("buildingType"))), likePattern)
            );
        }

        if (params.containsKey("district")) {
            String district = stringUtils.normalizeString(params.get("district").trim().toLowerCase());
            String likePattern = "%" + district + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("district"))), likePattern)
            );
        }
        if (params.containsKey("city")) {
            String city = stringUtils.normalizeString(params.get("city").trim().toLowerCase());
            String likePattern = "%" + city + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("city"))), likePattern)
            );
        }

        if (params.containsKey("ward")) {
            String ward = stringUtils.normalizeString(params.get("ward").trim().toLowerCase());
            String likePattern = "%" + ward + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("ward"))), likePattern)
            );
        }

        if (params.containsKey("street")) {
            String street = stringUtils.normalizeString(params.get("street").trim().toLowerCase());
            String likePattern = "%" + street + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, root.get("street")), likePattern)
            );
        }

        if (params.containsKey("minPrice") || params.containsKey("maxPrice")) {
            if (params.containsKey("minPrice")) {
                BigDecimal minPrice = new BigDecimal(params.get("minPrice"));
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice)
                );
            }
            if (params.containsKey("maxPrice")) {
                BigDecimal maxPrice = new BigDecimal(params.get("maxPrice"));
                spec = spec.and((root, query, criteriaBuilder) ->
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice)
                );
            }
        }


        if (params.containsKey("staffName")) {
            String staffName = stringUtils.normalizeString(params.get("staffName").trim().toLowerCase());
            String[] nameParts = staffName.split(" ");
            String likePatternFirstName = "%" + nameParts[0] + "%";
            String likePatternLastName = nameParts.length > 1 ? "%" + nameParts[1] + "%" : likePatternFirstName;
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, Consignment> customerJoin = root.join("customer", JoinType.INNER);
                Join<Customer, User> userJoin = customerJoin.join("users", JoinType.INNER);
                return criteriaBuilder.and(
                        criteriaBuilder.isNotNull(userJoin.get("userId")),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(userJoin.get("firstName"))), likePatternFirstName),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(userJoin.get("lastName"))), likePatternLastName)
                );
            });
        }

       if (params.containsKey("status")) {
            List<String> statuses = Arrays.asList(params.get("status").split(","));
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Consignment, ConsignmentStatusHistory> statusJoin = root.join("consignmentStatusHistories", JoinType.INNER);
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<ConsignmentStatusHistory> subqueryRoot = subquery.from(ConsignmentStatusHistory.class);
                subquery.select(criteriaBuilder.max(subqueryRoot.get("consignmentStatusHistoryId")))
                        .where(criteriaBuilder.equal(subqueryRoot.get("consignment"), root));
                return criteriaBuilder.and(
                        statusJoin.get("status").in(statuses),
                        criteriaBuilder.equal(statusJoin.get("consignmentStatusHistoryId"), subquery)
                );
            });
        }
        return spec;
    }

}
