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
import com.nqvinh.rentofficebackend.domain.common.service.NotiProducer;
import com.nqvinh.rentofficebackend.domain.common.service.NotificationService;
import com.nqvinh.rentofficebackend.domain.customer.constant.ConsignmentStatus;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentImageDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.response.CustomerResDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    NotiProducer notiProducer;
    NotificationService notificationService;
    CustomerService customerService;
    EmailProducer emailProducer;


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
    public ConsignmentDto updateConsignment(Long consignmentId, ConsignmentDto consignmentDto, List<MultipartFile> consignmentImages) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Consignment not found"));

        if (consignmentImages != null && !consignmentImages.isEmpty()) {
            consignmentImageService.updateConsignmentImages(consignment, consignmentImages);
        } else {
            consignmentDto.setConsignmentImages(consignment.getConsignmentImages().stream()
                    .map(consignmentImage -> ConsignmentImageDto.builder()
                            .consignmentImageId(consignmentImage.getConsignmentImageId())
                            .imgUrl(consignmentImage.getImgUrl())
                            .build())
                    .collect(Collectors.toList()));
        }

        consignmentMapper.partialUpdate(consignment, consignmentDto);
        consignment.getConsignmentImages().forEach(image -> image.setConsignment(consignment));

        if (consignmentDto.getStatus().equals(ConsignmentStatus.CANCELLED.toString())) {
            var mailCancelledConsignment = MailEvent.builder()
                    .toAddress(consignment.getCustomer().getEmail())
                    .subject("Từ chối tài sản ký gửi")
                    .templateName("cancelled-consignment-template")
                    .context(Map.of(
                            "customerName", consignment.getCustomer().getCustomerName(),
                            "price", consignment.getPrice(),
                            "city", consignment.getCity(),
                            "district", consignment.getDistrict(),
                            "ward", consignment.getWard(),
                            "street", consignment.getStreet(),
                            "rejectedReason", consignment.getRejectedReason()
                    ))
                    .status(MailStatus.INIT.getStatus())
                    .code(MessageCode.MAIL_CANCELLED_CONSIGNMENT.getCode())
                    .type(MailType.CONFIRMED_CONSIGNMENT.getType())
                    .build();
            emailProducer.sendMailCancelledConsignment(mailCancelledConsignment);
        }

       consignment.setUpdatedAt(LocalDateTime.now());

        if (consignmentDto.getStatus().equals(ConsignmentStatus.INCOMPLETE.toString())) {
            consignment.setAdditionalInfoAt(consignment.getUpdatedAt());
        } else if (consignmentDto.getStatus().equals(ConsignmentStatus.CONFIRMED.toString())) {
            consignment.setConfirmedAt(consignment.getUpdatedAt());
        } else if (consignmentDto.getStatus().equals(ConsignmentStatus.CANCELLED.toString())) {
            consignment.setRejectedReasonAt(consignment.getUpdatedAt());
        }

        return consignmentMapper.toDto(consignmentRepository.save(consignment));
    }


    @Override
    @SneakyThrows
    public ConsignmentDto getConsignmentById(Long consignmentId) {
        Consignment consignment = consignmentRepository.findById(consignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Consignment not found"));
        return consignmentMapper.toDto(consignment);
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
                   criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class,criteriaBuilder.lower( root.get("district"))), likePattern)
           );
       }
        if (params.containsKey("city")) {
            String city = stringUtils.normalizeString(params.get("city").trim().toLowerCase());
            String likePattern = "%" + city + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower( root.get("city"))), likePattern)
            );
        }

       if (params.containsKey("ward")) {
           String ward = stringUtils.normalizeString(params.get("ward").trim().toLowerCase());
           String likePattern = "%" + ward + "%";
           spec = spec.and((root, query, criteriaBuilder) ->
                   criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower( root.get("ward"))), likePattern)
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

        if(params.containsKey("status")){
            String status = params.get("status");
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status)
            );
        }

        return spec;
    }

}
