package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.domain.building.constant.BuildingStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.BuildingUnitStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.ConsignmentStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.OrientationEnum;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignBuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.dto.BuildingImageDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.dto.response.CustomerResDto;
import com.nqvinh.rentofficebackend.domain.building.entity.*;
import com.nqvinh.rentofficebackend.domain.building.mapper.BuildingMapper;
import com.nqvinh.rentofficebackend.domain.building.mapper.response.CustomerResMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingRepository;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingUnitRepository;
import com.nqvinh.rentofficebackend.domain.building.repository.CustomerRepository;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingImageService;
import com.nqvinh.rentofficebackend.domain.building.service.BuildingService;
import com.nqvinh.rentofficebackend.domain.building.service.CustomerService;
import com.nqvinh.rentofficebackend.domain.common.constant.MailStatus;
import com.nqvinh.rentofficebackend.domain.common.constant.MailType;
import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;
import com.nqvinh.rentofficebackend.domain.common.service.EmailProducer;
import com.nqvinh.rentofficebackend.domain.common.service.NotificationService;
import com.nqvinh.rentofficebackend.domain.common.service.RedisService;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BuildingServiceImpl implements BuildingService {

    CustomerRepository customerRepository;
    BuildingMapper buildingMapper;
    PaginationUtils paginationUtils;
    StringUtils stringUtils;
    BuildingRepository buildingRepository;
    BuildingImageService buildingImageService;
    CustomerResMapper customerResMapper;
    UserService userService;
    UserMapper userMapper;
    NotificationService notificationService;
    CustomerService customerService;
    EmailProducer emailProducer;
    RedisService redisService;
    UserRepository userRepository;
    BuildingUnitRepository buildingUnitRepository;

    @Override
    @SneakyThrows
    @Transactional
    public CustomerResDto createBuildingWithCustomer(CustomerReqDto customerReqDto, List<MultipartFile> buildingImages) {
        Customer customer = customerService.findOrCreateCustomer(customerReqDto);
        List<String> uploadedUrls = buildingImageService.uploadBuildingImages(customerReqDto, buildingImages);
        List<Building> newBuildings = buildingImageService.convertBuildingDtoToEntities(customerReqDto, uploadedUrls, customer);
        customer.getBuildings().addAll(newBuildings);

        sendMailNewConsignment(customer);
        Customer savedCustomer = customerRepository.save(customer);

        List<UserDto> adminsAndManagers = userService.getAllAdminsAndManagers();
        adminsAndManagers.forEach(adminOrManager -> notificationService.createBuildingNotification(adminOrManager, savedCustomer));
        return customerResMapper.toDto(savedCustomer);
    }

    @SneakyThrows
    private void sendMailNewConsignment(Customer customer) {

        var mailConfirmedConsignment = MailEvent.builder()
                .toAddress(customer.getEmail())
                .subject("Xác nhận tài sản ký gửi")
                .templateName("pending-consignment-template")
                .context(Map.of(
                        "customerName", customer.getCustomerName()
                ))
                .status(MailStatus.INIT.getStatus())
                .code(MessageCode.MAIL_PENDING_CONSIGNMENT.getCode())
                .type(MailType.PENDING_BUILDING.getType())
                .build();
        emailProducer.sendMailNewConsignment(mailConfirmedConsignment);
    }

    @Override
    @SneakyThrows
    public Page<BuildingDto> getCustomerBuildings(Map<String, String> params) {
        UserDto currentUser = userService.getLoggedInUser();
        Specification<Building> spec = getCustomerSpec(params);

        if (!isAdmin(currentUser)) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, Building> customerJoin = root.join("customer", JoinType.INNER);
                Join<Customer, User> userJoin = customerJoin.join("users", JoinType.INNER);
                return criteriaBuilder.equal(userJoin.get("userId"), currentUser.getUserId());
            });
        }

        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<Building> buildingPage = buildingRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(buildingPage, pageable);
        return paginationUtils.mapPage(buildingPage, meta, buildingMapper::toDto);
    }

    private boolean isAdmin(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        String roleName = user.getRole().getRoleName();
        return roleName.equals("ADMIN") || roleName.equals("MANAGER");
    }

    @Override
    @SneakyThrows
    public void deleteBuilding(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found"));
        buildingRepository.delete(building);
    }

    @Override
    @Transactional
    @SneakyThrows
    public BuildingDto updateBuilding(Long buildingId, BuildingDto buildingDto, List<MultipartFile> buildingImages, List<String> deletedImages) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found"));
        String token = UUID.randomUUID().toString();
        List<ConsignmentStatusHistory> existingStatusHistories = building.getConsignmentStatusHistories();
        List<RentalPricing> existingRentalPricing = building.getRentalPricing();
        List<PaymentPolicy> existingPaymentPolicies = building.getPaymentPolicies();
        List<Fee> existingFees = building.getFees();
        List<BuildingUnit> existingBuildingUnits = building.getBuildingUnits();

        String additionalInfoLink = "http://localhost:5173/ky-gui/" + buildingId + "?token=" + token;

        if (buildingDto.getConsignmentStatusHistories().getLast().getStatus().equals(ConsignmentStatus.INCOMPLETE.toString()) && building.getBuildingStatus() == null) {
            redisService.set("additional-info:" + buildingId, token);

            var mailIncompleteConsignment = MailEvent.builder()
                    .toAddress(building.getCustomer().getEmail())
                    .subject("Cập nhật thông tin tài sản ký gửi")
                    .templateName("incomplete-consignment-template")
                    .context(Map.of(
                            "customerName", buildingDto.getCustomer().getCustomerName(),
                            "additionalInfo", buildingDto.getConsignmentStatusHistories().getFirst().getNote(),
                            "additionalInfoLink", additionalInfoLink
                    ))
                    .status(MailStatus.INIT.getStatus())
                    .code(MessageCode.MAIL_INCOMPLETE_CONSIGNMENT.getCode())
                    .type(MailType.INCOMPLETE_CONSIGNMENT.getType())
                    .build();
            emailProducer.sendMailIncompleteConsignment(mailIncompleteConsignment);
        } else if (buildingDto.getConsignmentStatusHistories().getLast().getStatus().equals(ConsignmentStatus.ADDITIONAL_INFO.toString()) && building.getBuildingStatus() == null) {
            List<UserDto> users = userService.getAllUserByCustomerId(buildingDto.getCustomer().getCustomerId());
            users.forEach(user ->
                    notificationService.updateInfoBuildingNotification(user, buildingDto));
            redisService.delete("additional-info:" + buildingId);
        } else if (buildingDto.getConsignmentStatusHistories().getLast().getStatus().equals(ConsignmentStatus.CANCELLED.toString()) && building.getBuildingStatus() == null) {

            var mailCancelledConsignment = MailEvent.builder()
                    .toAddress(buildingDto.getCustomer().getEmail())
                    .subject("Từ chối tài sản ký gửi")
                    .templateName("cancelled-consignment-template")
                    .context(Map.of(
                            "customerName", buildingDto.getCustomer().getCustomerName(),
                            "rejectedReason", buildingDto.getConsignmentStatusHistories().getFirst().getNote()
                    ))
                    .status(MailStatus.INIT.getStatus())
                    .code(MessageCode.MAIL_CANCELLED_CONSIGNMENT.getCode())
                    .type(MailType.CANCELLED_BUILDING.getType())
                    .build();
            emailProducer.sendMailCancelledConsignment(mailCancelledConsignment);
        }else if(buildingDto.getConsignmentStatusHistories().getLast().getStatus().equals(ConsignmentStatus.CONFIRMED.toString()) && building.getBuildingStatus() == null){
            String generatedPassword = "12345678";
            buildingDto.setBuildingStatus(BuildingStatus.REVIEWING.toString());
            User newUser = userService.createUserForCustomer(buildingDto.getCustomer(), generatedPassword);
            String url = "http://localhost:5000/login";
            var mailConfirmedConsignment = MailEvent.builder()
                    .toAddress(buildingDto.getCustomer().getEmail())
                    .subject("Tài khoản quản lý tài sản ký gửi")
                    .templateName("confirmed-consignment-template")
                    .context(Map.of(
                            "customerName", buildingDto.getCustomer().getCustomerName(),
                            "email", newUser.getEmail(),
                            "generatedPassword", generatedPassword,
                            "loginUrl", url
                    ))
                    .status(MailStatus.INIT.getStatus())
                    .code(MessageCode.MAIL_CONFIRMED_CONSIGNMENT.getCode())
                    .type(MailType.CONFIRMED_BUILDING.getType())
                    .build();

            emailProducer.sendMailConfirmedConsignment(mailConfirmedConsignment);
        }

        if (buildingImages != null && !buildingImages.isEmpty()) {
            buildingImageService.updateBuildingImages(building, buildingImages);
        }
        if (deletedImages != null && !deletedImages.isEmpty()) {
            buildingImageService.deleteBuildingImages(building, deletedImages);
        }

        buildingDto.getConsignmentStatusHistories().stream()
                .filter(statusDto -> statusDto.getConsignmentStatusHistoryId() == null)
                .map(statusDto -> ConsignmentStatusHistory.builder()
                        .status(ConsignmentStatus.valueOf(statusDto.getStatus()))
                        .note(statusDto.getNote())
                        .building(building)
                        .build())
                .forEach(existingStatusHistories::add);
       buildingDto.getRentalPricing().stream()
                    .filter(price -> price.getRentalPricingId() == null)
                    .map(price -> RentalPricing.builder()
                            .price(price.getPrice())
                            .building(building)
                            .build())
                    .forEach(newPrice -> {
                        if (existingRentalPricing.stream().noneMatch(existingPrice -> existingPrice.getPrice().compareTo(newPrice.getPrice()) == 0)) {
                            existingRentalPricing.add(newPrice);
                        }
                    });
       buildingDto.getPaymentPolicies().stream()
                    .filter(policy -> policy.getPaymentPolicyId() == null)
                    .map(policy -> PaymentPolicy.builder()
                            .paymentCycle(policy.getPaymentCycle())
                            .depositTerm(policy.getDepositTerm())
                            .building(building)
                            .build())
                    .forEach( newPolicy -> {
                        if (existingPaymentPolicies.stream().noneMatch(existingPolicy -> existingPolicy.getDepositTerm().equals(newPolicy.getDepositTerm()) && existingPolicy.getPaymentCycle().equals(newPolicy.getPaymentCycle()))) {
                            existingPaymentPolicies.add(newPolicy);
                        }
                    });


        List<Fee> feesToRemove = new ArrayList<>();
        for (Fee existingFee : existingFees) {
            boolean isFeeExistInDto = buildingDto.getFees().stream()
                    .anyMatch(feeDto -> feeDto.getFeeId().equals(existingFee.getFeeId()));
            if (!isFeeExistInDto) {
                feesToRemove.add(existingFee);
            }
        }
        existingFees.removeAll(feesToRemove);
        buildingDto.getFees().forEach(feeDto -> {
            Fee fee = existingFees.stream()
                    .filter(existingFee -> Objects.equals(existingFee.getFeeId(), feeDto.getFeeId()))
                    .findFirst()
                    .orElseGet(() -> {

                        Fee newFee = Fee.builder()
                                .feeType(FeeType.builder().feeTypeId(feeDto.getFeeType().getFeeTypeId()).build())
                                .building(building)
                                .feePricing(new ArrayList<>())
                                .build();
                        existingFees.add(newFee);
                        return newFee;
                    });

            feeDto.getFeePricing().forEach(feePricingDto -> {
                Optional<FeePricing> existingPricingOpt = fee.getFeePricing().stream()
                        .filter(existingPricing ->
                                Objects.equals(existingPricing.getPriceUnit(), feePricingDto.getPriceUnit()) ||
                                        Objects.equals(existingPricing.getPriceValue(), feePricingDto.getPriceValue()) ||
                                        Objects.equals(existingPricing.getDescription(), feePricingDto.getDescription()))
                        .findFirst();

                if (existingPricingOpt.isPresent()) {
                    FeePricing existingPricing = existingPricingOpt.get();
                    existingPricing.setPriceValue(feePricingDto.getPriceValue());
                    existingPricing.setPriceUnit(feePricingDto.getPriceUnit());
                    existingPricing.setDescription(feePricingDto.getDescription());
                } else {
                    // Nếu FeePricing không tồn tại, tạo mới một FeePricing
                    FeePricing newFeePricing = FeePricing.builder()
                            .priceUnit(feePricingDto.getPriceUnit())
                            .priceValue(feePricingDto.getPriceValue())
                            .description(feePricingDto.getDescription())
                            .fee(fee) // Liên kết với Fee
                            .build();
                    fee.getFeePricing().add(newFeePricing); // Thêm FeePricing mới vào danh sách
                }
            });
        });

//       List<BuildingUnit> buildingUnitsToRemove = new ArrayList<>();
//        for (BuildingUnit existingUnit : building.getBuildingUnits()) {
//            boolean isUnitExistInDto = buildingDto.getBuildingUnits().stream()
//                    .anyMatch(unitDto -> unitDto.getBuildingUnitId().equals(existingUnit.getBuildingUnitId()));
//            if (!isUnitExistInDto) {
//                buildingUnitsToRemove.add(existingUnit);
//            } else {
//                List<RentArea> rentAreasToRemove = new ArrayList<>();
//                for (RentArea existingRentArea : existingUnit.getRentAreas()) {
//                    boolean isRentAreaExistInDto = buildingDto.getBuildingUnits().stream()
//                            .flatMap(unitDto -> unitDto.getRentAreas().stream())
//                            .anyMatch(rentAreaDto -> rentAreaDto.getRentAreaId().equals(existingRentArea.getRentAreaId()));
//                    if (!isRentAreaExistInDto) {
//                        rentAreasToRemove.add(existingRentArea);
//                    }
//                }
//                existingUnit.getRentAreas().removeAll(rentAreasToRemove);
//            }
//        }
//        existingBuildingUnits.removeAll(buildingUnitsToRemove);
//
//        buildingDto.getBuildingUnits().forEach(unitDto -> {
//            BuildingUnit unit = existingBuildingUnits.stream()
//                    .filter(existingUnit -> Objects.equals(existingUnit.getBuildingUnitId(), unitDto.getBuildingUnitId()))
//                    .findFirst()
//                    .orElseGet(() -> {
//                        BuildingUnit newUnit = BuildingUnit.builder()
//                                .buildingUnitStatus(BuildingUnitStatus.valueOf(unitDto.getBuildingUnitStatus()))
//                                .floor(unitDto.getFloor())
//                                .unitName(unitDto.getUnitName())
//                                .rentAreas(new ArrayList<>())
//                                .building(building)
//                                .build();
//                        existingBuildingUnits.add(newUnit);
//                        return newUnit;
//                    });
//            unitDto.getRentAreas().forEach(unitRentAreaDto -> {
//                Optional<RentArea> existingRentAreaOpt = unit.getRentAreas().stream()
//                        .filter(existingRentArea ->
//                                Objects.equals(existingRentArea.getRentAreaId(), unitRentAreaDto.getRentAreaId()))
//                        .findFirst();
//                if (existingRentAreaOpt.isPresent()) {
//                    RentArea existingRentArea = existingRentAreaOpt.get();
//                    existingRentArea.setArea(unitRentAreaDto.getArea());
//                } else {
//                    RentArea newRentArea = RentArea.builder()
//                            .area(unitRentAreaDto.getArea())
//                            .buildingUnit(unit)
//                            .build();
//                    unit.getRentAreas().add(newRentArea);
//                }
//            });
//        });

        List<BuildingUnit> buildingUnitsToRemove = new ArrayList<>();
        for (BuildingUnit existingUnit : building.getBuildingUnits()) {
            boolean isUnitExistInDto = buildingDto.getBuildingUnits().stream()
                    .anyMatch(unitDto -> unitDto.getBuildingUnitId().equals(existingUnit.getBuildingUnitId()));
            if (!isUnitExistInDto) {
                buildingUnitsToRemove.add(existingUnit);
            } else {
                List<RentArea> rentAreasToRemove = new ArrayList<>();
                for (RentArea existingRentArea : existingUnit.getRentAreas()) {
                    boolean isRentAreaExistInDto = buildingDto.getBuildingUnits().stream()
                            .flatMap(unitDto -> unitDto.getRentAreas().stream())
                            .anyMatch(rentAreaDto -> rentAreaDto.getRentAreaId().equals(existingRentArea.getRentAreaId()));
                    if (!isRentAreaExistInDto) {
                        rentAreasToRemove.add(existingRentArea);
                    }
                }
                existingUnit.getRentAreas().removeAll(rentAreasToRemove);
            }
        }
        building.getBuildingUnits().removeAll(buildingUnitsToRemove);

        buildingDto.getBuildingUnits().forEach(unitDto -> {
            BuildingUnit unit = building.getBuildingUnits().stream()
                    .filter(existingUnit -> Objects.equals(existingUnit.getBuildingUnitId(), unitDto.getBuildingUnitId()))
                    .findFirst()
                    .orElseGet(() -> {
                        BuildingUnit newUnit = BuildingUnit.builder()
                                .buildingUnitStatus(BuildingUnitStatus.valueOf(unitDto.getBuildingUnitStatus()))
                                .floor(unitDto.getFloor())
                                .unitName(unitDto.getUnitName())
                                .rentAreas(new ArrayList<>())
                                .building(building)
                                .build();
                        building.getBuildingUnits().add(newUnit);
                        return newUnit;
                    });
            buildingUnitRepository.saveAndFlush(unit);
            unit.getRentAreas().clear();
            unitDto.getRentAreas().forEach(unitRentAreaDto -> {
                RentArea newRentArea = RentArea.builder()
                        .area(unitRentAreaDto.getArea())
                        .buildingUnit(unit)
                        .build();
                unit.getRentAreas().add(newRentArea);
            });
        });



        buildingDto.setBuildingImages(building.getBuildingImages().stream()
                .map(image -> BuildingImageDto.builder()
                        .buildingImageId(image.getBuildingImageId())
                        .imgUrl(image.getImgUrl())
                        .build())
                .collect(Collectors.toList()));


        building.setPaymentPolicies(existingPaymentPolicies);
        building.setConsignmentStatusHistories(existingStatusHistories);
        building.setRentalPricing(existingRentalPricing);
        building.setFees(existingFees);
        building.setBuildingUnits(existingBuildingUnits);



        buildingMapper.partialUpdate(building, buildingDto);
        building.getFees().forEach(fee -> fee.setBuilding(building));
        building.getRentalPricing().forEach(price -> price.setBuilding(building));
        building.getPaymentPolicies().forEach(policy -> policy.setBuilding(building));
        building.getBuildingImages().forEach(image -> image.setBuilding(building));
        building.getBuildingUnits().forEach(unit -> unit.setBuilding(building));
        building.getConsignmentStatusHistories().forEach(status -> status.setBuilding(building));
        Building savedConsignment = buildingRepository.save(building);
        return buildingMapper.toDto(savedConsignment);
    }

    @Override
    @SneakyThrows
    public BuildingDto getBuildingById(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Consignment not found"));
        return buildingMapper.toDto(building);
    }

    @Override
    @SneakyThrows
    public void verifyTokenBuilding(String buildingId, String token) {
        String tokenInRedis = redisService.get("additional-info:" + buildingId).toString();
        if (!token.equals(tokenInRedis)) {
            throw new ResourceNotFoundException("Token is invalid");
        }
    }

    @Override
    @SneakyThrows
    public Page<BuildingDto> getBuildings(Map<String, String> params) {
        UserDto currentUser = userService.getLoggedInUser();
        Specification<Building> spec = getBuildingSpec(params);

        if (!isAdmin(currentUser)) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, Building> customerJoin = root.join("customer", JoinType.INNER);
                Join<Customer, User> userJoin = customerJoin.join("users", JoinType.INNER);
                return criteriaBuilder.equal(userJoin.get("userId"), currentUser.getUserId());
            });
        }

        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<Building> buildingPage = buildingRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(buildingPage, pageable);
        return paginationUtils.mapPage(buildingPage, meta, buildingMapper::toDto);
    }

    @Override
    public List<BuildingDto> getAllBuildingCompany() {
        return buildingMapper.toDtoList(buildingRepository.findAllByBuildingStatusIsNotNull());
    }

    @Override
    @SneakyThrows
    @Transactional
    public void assignmentBuildingToStaffs(AssignBuildingDto assignBuildingDto) {
        Building building = buildingRepository.findById(assignBuildingDto.getBuilding().getBuildingId())
                .orElseThrow(() -> new ResourceNotFoundException("Building not found"));
        List<User> users = assignBuildingDto.getUsers().stream()
                .map(userDto -> {
                    try {
                        return userRepository.findById(userDto.getUserId())
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
                    } catch (ResourceNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        building.setUsers(userService.getUsersByIdIn(userMapper.toDtoList(users)));
        notificationService.assignBuildingToStaffs(assignBuildingDto);
        buildingRepository.save(building);
    }

    @Override
    @SneakyThrows
    public List<UserDto> getStaffsByBuildingId(Long buildingId) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Building not found"));
        List<UserDto> userDto = userService.loadStaff();
        return userDto.stream()
                .peek(user -> {
                    if (building.getUsers().stream().anyMatch(buildingUser -> buildingUser.getUserId().equals(user.getUserId()))) {
                        user.setChecked("checked");
                    }
                })
                .collect(Collectors.toList());
    }

    private Specification<Building> getBuildingSpec(Map<String, String> params) {
        Specification<Building> spec = (root, query, cb) -> cb.isNotNull(root.get("buildingStatus"));

        if(params.containsKey("orientation")){
            String orientation = params.get("orientation").trim().toUpperCase();
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("orientation"), OrientationEnum.valueOf(orientation.toUpperCase()))
            );
        }

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

        if (params.containsKey("buildingName")) {
            String customerName = stringUtils.normalizeString(params.get("buildingName").trim().toLowerCase());
            String likePattern = "%" + customerName + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("buildingName"))), likePattern)
            );
        }

        if (params.containsKey("phoneNumber")) {
            String phoneNumber = stringUtils.normalizeString(params.get("phoneNumber").trim().toLowerCase());
            spec = spec.and((root, query, cb) -> {
                Join<Customer, Building> customerJoin = root.join("customer", JoinType.INNER);
                return cb.equal(customerJoin.get("phoneNumber"), phoneNumber);
            });
        }

        if (params.containsKey("buildingType")) {
            String buildingType = stringUtils.normalizeString(params.get("buildingType").trim().toLowerCase());
            String likePattern = "%" + buildingType + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("buildingType").get("buildingTypeName"))), likePattern)
            );
        }

        if (params.containsKey("buildingLevel")) {
            String buildingType = stringUtils.normalizeString(params.get("buildingLevel").trim().toLowerCase());
            String likePattern = "%" + buildingType + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("buildingLevel").get("buildingLevelName"))), likePattern)
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
                spec = spec.and((root, query, criteriaBuilder) -> {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<RentalPricing> rentalPricingRoot = subquery.from(RentalPricing.class);

                    subquery.select(criteriaBuilder.max(rentalPricingRoot.get("rentalPricingId")))
                            .where(criteriaBuilder.equal(rentalPricingRoot.get("building"), root));

                    Subquery<BigDecimal> priceSubquery = query.subquery(BigDecimal.class);
                    Root<RentalPricing> rentalPricingRoot2 = priceSubquery.from(RentalPricing.class);
                    priceSubquery.select(rentalPricingRoot2.get("price"))
                            .where(criteriaBuilder.equal(rentalPricingRoot2.get("rentalPricingId"), subquery));

                    return criteriaBuilder.greaterThanOrEqualTo(priceSubquery, minPrice);
                });
            }

            if (params.containsKey("maxPrice")) {
                BigDecimal maxPrice = new BigDecimal(params.get("maxPrice"));
                spec = spec.and((root, query, criteriaBuilder) -> {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<RentalPricing> rentalPricingRoot = subquery.from(RentalPricing.class);

                    subquery.select(criteriaBuilder.max(rentalPricingRoot.get("rentalPricingId")))
                            .where(criteriaBuilder.equal(rentalPricingRoot.get("building"), root));

                    Subquery<BigDecimal> priceSubquery = query.subquery(BigDecimal.class);
                    Root<RentalPricing> rentalPricingRoot2 = priceSubquery.from(RentalPricing.class);
                    priceSubquery.select(rentalPricingRoot2.get("price"))
                            .where(criteriaBuilder.equal(rentalPricingRoot2.get("rentalPricingId"), subquery));

                    return criteriaBuilder.lessThanOrEqualTo(priceSubquery, maxPrice);
                });
            }
        }


        if (params.containsKey("staffName")) {
            String staffName = stringUtils.normalizeString(params.get("staffName").trim().toLowerCase());
            String[] nameParts = staffName.split("\\s+");

            String likePatternFirstName = nameParts.length > 1
                    ? "%" + String.join(" ", Arrays.copyOfRange(nameParts, 0, nameParts.length - 1)) + "%"
                    : "%" + nameParts[0] + "%";
            String likePatternLastName = "%" + nameParts[nameParts.length - 1] + "%";
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, Building> customerJoin = root.join("customer", JoinType.INNER);
                Join<Customer, User> userJoin = customerJoin.join("users", JoinType.INNER);
                return criteriaBuilder.and(
                        criteriaBuilder.isNotNull(userJoin.get("userId")),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(userJoin.get("firstName"))), likePatternFirstName),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(userJoin.get("lastName"))), likePatternLastName)
                );
            });
        }

        if (params.containsKey("buildingStatus")) {
            String buildingStatus = params.get("buildingStatus").trim().toUpperCase();
            spec = spec.and((root, query, cb) -> cb.equal(root.get("buildingStatus"), buildingStatus));
        }
        return spec;
    }


    private Specification<Building> getCustomerSpec(Map<String, String> params) {
        Specification<Building> spec = Specification.where(null);

        if(params.containsKey("orientation")){
            String orientation = params.get("orientation").trim().toUpperCase();
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("orientation"), OrientationEnum.valueOf(orientation.toUpperCase()))
            );
        }

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
                Join<Customer, Building> customerJoin = root.join("customer", JoinType.INNER);
                return cb.equal(customerJoin.get("phoneNumber"), phoneNumber);
            });
        }

        if (params.containsKey("buildingType")) {
            String buildingType = stringUtils.normalizeString(params.get("buildingType").trim().toLowerCase());
            String likePattern = "%" + buildingType + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("buildingType").get("buildingTypeName"))), likePattern)
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
                spec = spec.and((root, query, criteriaBuilder) -> {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<RentalPricing> rentalPricingRoot = subquery.from(RentalPricing.class);

                    subquery.select(criteriaBuilder.max(rentalPricingRoot.get("rentalPricingId")))
                            .where(criteriaBuilder.equal(rentalPricingRoot.get("building"), root));

                    Subquery<BigDecimal> priceSubquery = query.subquery(BigDecimal.class);
                    Root<RentalPricing> rentalPricingRoot2 = priceSubquery.from(RentalPricing.class);
                    priceSubquery.select(rentalPricingRoot2.get("price"))
                            .where(criteriaBuilder.equal(rentalPricingRoot2.get("rentalPricingId"), subquery));

                    return criteriaBuilder.greaterThanOrEqualTo(priceSubquery, minPrice);
                });
            }

            if (params.containsKey("maxPrice")) {
                BigDecimal maxPrice = new BigDecimal(params.get("maxPrice"));
                spec = spec.and((root, query, criteriaBuilder) -> {
                    Subquery<Long> subquery = query.subquery(Long.class);
                    Root<RentalPricing> rentalPricingRoot = subquery.from(RentalPricing.class);

                    subquery.select(criteriaBuilder.max(rentalPricingRoot.get("rentalPricingId")))
                            .where(criteriaBuilder.equal(rentalPricingRoot.get("building"), root));

                    Subquery<BigDecimal> priceSubquery = query.subquery(BigDecimal.class);
                    Root<RentalPricing> rentalPricingRoot2 = priceSubquery.from(RentalPricing.class);
                    priceSubquery.select(rentalPricingRoot2.get("price"))
                            .where(criteriaBuilder.equal(rentalPricingRoot2.get("rentalPricingId"), subquery));

                    return criteriaBuilder.lessThanOrEqualTo(priceSubquery, maxPrice);
                });
            }
        }


        if (params.containsKey("staffName")) {
            String staffName = stringUtils.normalizeString(params.get("staffName").trim().toLowerCase());
            String[] nameParts = staffName.split("\\s+");

            String likePatternFirstName = nameParts.length > 1
                    ? "%" + String.join(" ", Arrays.copyOfRange(nameParts, 0, nameParts.length - 1)) + "%"
                    : "%" + nameParts[0] + "%";
            String likePatternLastName = "%" + nameParts[nameParts.length - 1] + "%";
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, Building> customerJoin = root.join("customer", JoinType.INNER);
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
                Join<Building, ConsignmentStatusHistory> statusJoin = root.join("consignmentStatusHistories", JoinType.INNER);
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<ConsignmentStatusHistory> subqueryRoot = subquery.from(ConsignmentStatusHistory.class);
                subquery.select(criteriaBuilder.max(subqueryRoot.get("consignmentStatusHistoryId")))
                        .where(criteriaBuilder.equal(subqueryRoot.get("building"), root));
                return criteriaBuilder.and(
                        statusJoin.get("status").in(statuses),
                        criteriaBuilder.equal(statusJoin.get("consignmentStatusHistoryId"), subquery)
                );
            });
        }
        return spec;
    }

}
