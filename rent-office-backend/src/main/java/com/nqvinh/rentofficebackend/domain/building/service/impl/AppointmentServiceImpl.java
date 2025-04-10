/*******************************************************************************
 * Class        ：AppointmentServiceImpl
 * Created date ：2025/03/25
 * Lasted date  ：2025/03/25
 * Author       ：vinhNQ2
 * Change log   ：2025/03/25：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.domain.building.constant.AppointmentBuildingStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.PotentialCustomerStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.RequireTypeEnum;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.calendar.AppointmentBuildingCalendarDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.AppointmentBuildingReqDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.AppointmentReqDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.CustomerAppointmentReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.*;
import com.nqvinh.rentofficebackend.domain.building.mapper.appointment.calendar.AppointmentBuildingCalendarMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.AppointmentBuildingRepository;
import com.nqvinh.rentofficebackend.domain.building.repository.AppointmentRepository;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingRepository;
import com.nqvinh.rentofficebackend.domain.building.repository.CustomerRepository;
import com.nqvinh.rentofficebackend.domain.building.service.AppointmentService;
import com.nqvinh.rentofficebackend.domain.common.constant.MailStatus;
import com.nqvinh.rentofficebackend.domain.common.constant.MailType;
import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;
import com.nqvinh.rentofficebackend.domain.common.service.EmailProducer;
import com.nqvinh.rentofficebackend.domain.common.service.NotificationService;
import com.nqvinh.rentofficebackend.infrastructure.utils.DateUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

/**
 * AppointmentServiceImpl
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppointmentServiceImpl implements AppointmentService {
    CustomerRepository customerRepository;
    BuildingRepository buildingRepository;
    EmailProducer emailProducer;
    UserService userService;
    NotificationService notificationService;
    AppointmentBuildingRepository appointmentBuildingRepository;
    AppointmentBuildingCalendarMapper appointmentBuildingCalendarMapper;
    UserMapper userMapper;
    PaginationUtils paginationUtils;
    DateUtils dateUtils;
    AppointmentRepository appointmentRepository;

    private boolean isAdmin(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        String roleName = user.getRole().getRoleName();
        return roleName.equals("ADMIN") || roleName.equals("MANAGER");
    }

    @Transactional
    @Override
    @SneakyThrows
    public void createAppointment(CustomerAppointmentReqDto appointmentReq) {
        Customer customer = customerRepository.findByEmail(appointmentReq.getEmail())
                .map(existingCustomer -> {
                    existingCustomer.setNote(appointmentReq.getNote());
                    return existingCustomer;
                })
                .orElseGet(() -> Customer.builder()
                        .customerName(appointmentReq.getCustomerName())
                        .phoneNumber(appointmentReq.getPhoneNumber())
                        .email(appointmentReq.getEmail())
                        .address(appointmentReq.getAddress())
                        .requireType(RequireTypeEnum.RENT)
                        .status(PotentialCustomerStatus.NOT_CONTACTED)
                        .note(appointmentReq.getNote())
                        .appointments(new ArrayList<>())
                        .build());

        if (appointmentReq.getAppointments() != null) {
            for (AppointmentReqDto appointmentReqDto : appointmentReq.getAppointments()) {
                Appointment appointment = Appointment.builder()
                        .customer(customer)
                        .appointmentBuildings(new ArrayList<>())
                        .build();

                if (appointmentReqDto.getAppointmentBuildings() != null) {
                    for (AppointmentBuildingReqDto buildingDto : appointmentReqDto.getAppointmentBuildings()) {
                        Building building = buildingRepository.findById(buildingDto.getBuildingId())
                                .orElseThrow(() -> new ResourceNotFoundException("Building not found with id: " + buildingDto.getBuildingId()));

                        AppointmentBuilding appointmentBuilding = AppointmentBuilding.builder()
                                .appointment(appointment)
                                .building(building)
                                .area(buildingDto.getArea())
                                .visitTime(buildingDto.getVisitTime())
                                .appointmentBuildingStatusHistories(new ArrayList<>())
                                .build();

                        AppointmentBuildingStatusHistory buildingStatusHistory = AppointmentBuildingStatusHistory.builder()
                                .appointmentBuilding(appointmentBuilding)
                                .status(AppointmentBuildingStatus.PENDING)
                                .build();

                        appointmentBuilding.getAppointmentBuildingStatusHistories().add(buildingStatusHistory);

                        appointment.getAppointmentBuildings().add(appointmentBuilding);
                    }

                }

                customer.getAppointments().add(appointment);
            }
        }

        customerRepository.save(customer);

        var mailAppointment = MailEvent.builder()
                .toAddress(customer.getEmail())
                .subject("Xác nhận lịch hẹn")
                .templateName("confirm-appointment-template")
                .context(Map.of(
                        "customerName", customer.getCustomerName()
                ))
                .status(MailStatus.INIT.getStatus())
                .code(MessageCode.MAIL_APPOINTMENT.getCode())
                .type(MailType.CONFIRM_APPOINTMENT.getType())
                .build();
        emailProducer.sendMailAppointment(mailAppointment);
        List<UserDto> adminsAndManagers = userService.getAllAdminsAndManagers();
        adminsAndManagers.forEach(adminOrManager -> notificationService.createAppointment(adminOrManager, customer));
    }

    @Override
    @SneakyThrows
    public Map<LocalDateTime, List<AppointmentBuildingCalendarDto>> getAllAppointmentCalendars() {
        UserDto currentUser = userService.getLoggedInUser();
        boolean isAdmin = isAdmin(currentUser);
        return appointmentBuildingRepository.findAllAssignedWithPrivilege(currentUser.getUserId(), isAdmin).stream()
                .collect(groupingBy(AppointmentBuilding::getVisitTime, mapping(appointmentBuildingCalendarMapper::toDto, toList())));
    }

    @Override
    @SneakyThrows
    public Page<AppointmentBuildingCalendarDto> getAllAppointmentCalendars(Map<String, String> params) {
        UserDto currentUser = userService.getLoggedInUser();
        Specification<AppointmentBuilding> spec = getBuildingSpec(params);

        if (!isAdmin(currentUser)) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<AppointmentBuilding, Appointment> appointmentJoin = root.join("appointment", JoinType.INNER);
                Join<Appointment, Customer> customerJoin = appointmentJoin.join("customer", JoinType.INNER);
                Join<Customer, User> userJoin = customerJoin.join("users", JoinType.INNER);
                return criteriaBuilder.equal(userJoin.get("userId"), currentUser.getUserId());
            });
        }

        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<AppointmentBuilding> buildingPage = appointmentBuildingRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(buildingPage, pageable);
        return paginationUtils.mapPage(buildingPage, meta, appointmentBuildingCalendarMapper::toDto);
    }

    @Override
    @SneakyThrows
    public AppointmentBuildingCalendarDto getAppointmentCalendarById(Long appointmentBuildingId) {
        return appointmentBuildingRepository.findById(appointmentBuildingId)
                .map(appointmentBuildingCalendarMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentBuildingId));
    }

    @Override
    @SneakyThrows
    public void deleteAppointmentCalendarById(Long appointmentBuildingId) {
        AppointmentBuilding appointmentBuilding = appointmentBuildingRepository.findById(appointmentBuildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentBuildingId));

        Appointment appointment = appointmentBuilding.getAppointment();
        appointmentBuildingRepository.delete(appointmentBuilding);

        if (appointment.getAppointmentBuildings().isEmpty()) {
            appointmentRepository.delete(appointment);
        }
    }

    @Override
    @Transactional
    @SneakyThrows
    public void createAppointmentAdmin(CustomerAppointmentReqDto appointmentReq) {
        Customer customer = customerRepository.findByEmail(appointmentReq.getEmail())
                .map(existingCustomer -> {
                    existingCustomer.setNote(appointmentReq.getNote());
                    return existingCustomer;
                })
                .orElseGet(() -> Customer.builder()
                        .customerName(appointmentReq.getCustomerName())
                        .phoneNumber(appointmentReq.getPhoneNumber())
                        .email(appointmentReq.getEmail())
                        .address(appointmentReq.getAddress())
                        .requireType(RequireTypeEnum.RENT)
                        .status(PotentialCustomerStatus.NOT_CONTACTED)
                        .note(appointmentReq.getNote())
                        .appointments(new ArrayList<>())
                        .build());

        if (appointmentReq.getAppointments() != null) {
            for (AppointmentReqDto appointmentReqDto : appointmentReq.getAppointments()) {
                Appointment appointment = Appointment.builder()
                        .customer(customer)
                        .appointmentBuildings(new ArrayList<>())
                        .build();

                if (appointmentReqDto.getAppointmentBuildings() != null) {
                    for (AppointmentBuildingReqDto buildingDto : appointmentReqDto.getAppointmentBuildings()) {
                        Building building = buildingRepository.findById(buildingDto.getBuildingId())
                                .orElseThrow(() -> new ResourceNotFoundException("Building not found with id: " + buildingDto.getBuildingId()));

                        AppointmentBuilding appointmentBuilding = AppointmentBuilding.builder()
                                .appointment(appointment)
                                .building(building)
                                .area(buildingDto.getArea())
                                .visitTime(buildingDto.getVisitTime())
                                .appointmentBuildingStatusHistories(new ArrayList<>())
                                .build();

                        AppointmentBuildingStatusHistory buildingStatusHistory = AppointmentBuildingStatusHistory.builder()
                                .appointmentBuilding(appointmentBuilding)
                                .status(AppointmentBuildingStatus.PENDING)
                                .build();

                        appointmentBuilding.getAppointmentBuildingStatusHistories().add(buildingStatusHistory);

                        appointment.getAppointmentBuildings().add(appointmentBuilding);
                    }

                }

                customer.getAppointments().add(appointment);
            }
        }

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    @SneakyThrows
    public AppointmentBuildingCalendarDto updateAppointmentCalendarById(Long appointmentBuildingId, AppointmentBuildingCalendarDto appointmentBuildingCalendarDto) {
        AppointmentBuilding appointmentBuilding = appointmentBuildingRepository.findById(appointmentBuildingId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentBuildingId));
        List<AppointmentBuildingStatusHistory> existingStatusHistories = appointmentBuilding.getAppointmentBuildingStatusHistories();
        appointmentBuildingCalendarDto.getAppointmentBuildingStatusHistories().stream().
                filter(appointmentBuildingStatusHistory -> appointmentBuildingStatusHistory != null && appointmentBuildingStatusHistory.getAppointmentBuildingStatusHistoryId() == null)
                .map(appointmentBuildingStatusHistory ->
                        AppointmentBuildingStatusHistory.builder()
                                .appointmentBuilding(appointmentBuilding)
                                .status(AppointmentBuildingStatus.valueOf(appointmentBuildingStatusHistory.getStatus()))
                                .note(appointmentBuildingStatusHistory.getNote())
                                .build()
                )
                .forEach(existingStatusHistories::add);
        appointmentBuilding.setAppointmentBuildingStatusHistories(existingStatusHistories);
        appointmentBuildingCalendarMapper.partialUpdate(appointmentBuilding, appointmentBuildingCalendarDto);
        appointmentBuilding.getAppointmentBuildingStatusHistories().forEach(status -> status.setAppointmentBuilding(appointmentBuilding));

        return appointmentBuildingCalendarMapper.toDto(appointmentBuildingRepository.save(appointmentBuilding));
    }

    @Override
    public Map<String, Object> getAppointmentStatistics(Map<String, String> params) {
        Specification<AppointmentBuilding> spec = getAppointmentStatisticSpec(params);

        Map<String, Object> statistics = new HashMap<>();
        List<AppointmentBuilding> appointments = appointmentBuildingRepository.findAll(spec);


        statistics.put("pending", appointments.stream()
                .filter(appointment -> appointment.getAppointmentBuildingStatusHistories().stream()
                        .max(Comparator.comparing(AppointmentBuildingStatusHistory::getCreatedAt))
                        .map(AppointmentBuildingStatusHistory::getStatus)
                        .orElse(null) == AppointmentBuildingStatus.PENDING)
                .count());
        statistics.put("confirmed", appointments.stream()
                .filter(appointment -> appointment.getAppointmentBuildingStatusHistories().stream()
                        .max(Comparator.comparing(AppointmentBuildingStatusHistory::getCreatedAt))
                        .map(AppointmentBuildingStatusHistory::getStatus)
                        .orElse(null) == AppointmentBuildingStatus.CONFIRMED)
                .count());
        statistics.put("viewed", appointments.stream()
                .filter(appointment -> appointment.getAppointmentBuildingStatusHistories().stream()
                        .max(Comparator.comparing(AppointmentBuildingStatusHistory::getCreatedAt))
                        .map(AppointmentBuildingStatusHistory::getStatus)
                        .orElse(null) == AppointmentBuildingStatus.VIEWED)
                .count());
        statistics.put("successful", appointments.stream()
                .filter(appointment -> appointment.getAppointmentBuildingStatusHistories().stream()
                        .max(Comparator.comparing(AppointmentBuildingStatusHistory::getCreatedAt))
                        .map(AppointmentBuildingStatusHistory::getStatus)
                        .orElse(null) == AppointmentBuildingStatus.SUCCESSFUL)
                .count());
        statistics.put("unsuccessful", appointments.stream()
                .filter(appointment -> appointment.getAppointmentBuildingStatusHistories().stream()
                        .max(Comparator.comparing(AppointmentBuildingStatusHistory::getCreatedAt))
                        .map(AppointmentBuildingStatusHistory::getStatus)
                        .orElse(null) == AppointmentBuildingStatus.UNSUCCESSFUL)
                .count());
        statistics.put("cancelled", appointments.stream()
                .filter(appointment -> appointment.getAppointmentBuildingStatusHistories().stream()
                        .max(Comparator.comparing(AppointmentBuildingStatusHistory::getCreatedAt))
                        .map(AppointmentBuildingStatusHistory::getStatus)
                        .orElse(null) == AppointmentBuildingStatus.CANCELLED)
                .count());
        return statistics;
    }

    @Override
    public Map<String, Object> getAppointmentStatisticsByTime(Map<String, String> params) {
        Specification<AppointmentBuilding> spec = getAppointmentStatisticSpecTime(params);
        String type = params.getOrDefault("type", "");
        String startDateStr = params.getOrDefault("startDate", null);
        String endDateStr = params.getOrDefault("endDate", null);

        Map<String, BigDecimal> appointmentStatistics = new TreeMap<>();
        if (type.equals("year")) {
            int currentYear = LocalDate.now().getYear();
            IntStream.rangeClosed(1, 12).forEach(month -> {
                String key = YearMonth.of(currentYear, month).toString(); // Đảm bảo trả về đúng tháng
                appointmentStatistics.put(key, BigDecimal.ZERO); // Gán giá trị 0 nếu không có dữ liệu
            });
        }

        if (type.equals("month")) {
            LocalDate now = LocalDate.now();
            int requestedMonth = Integer.parseInt(startDateStr.split("-")[1]);
            YearMonth yearMonth = YearMonth.of(now.getYear(), requestedMonth);

            int currentDateOfMonth = (now.getMonthValue() == requestedMonth) ? now.getDayOfMonth() : yearMonth.lengthOfMonth();

            IntStream.rangeClosed(1, currentDateOfMonth).forEach(day -> {
                String key = yearMonth.atDay(day).toString(); // Đảm bảo tạo mốc ngày đầy đủ
                appointmentStatistics.put(key, BigDecimal.ZERO); // Gán giá trị 0 nếu không có dữ liệu
            });
        }

        if (type.equals("quarter")) {
            // Xử lý cho quý
            LocalDate startDate = dateUtils.parseDate(startDateStr, "quarter");
            LocalDate endDate = startDate.plusMonths(3).withDayOfMonth(1).minusDays(1);
            while (!startDate.isAfter(endDate)) {
                String key = startDate.toString();
                appointmentStatistics.put(key, BigDecimal.ZERO); // Đảm bảo trả về tất cả các mốc thời gian trong quý
                startDate = startDate.plusDays(1);
            }
        }

        if (startDateStr != null && endDateStr != null && type.equals("date")) {
            LocalDate startDate = dateUtils.parseDate(startDateStr, "date");
            LocalDate endDate = dateUtils.parseDate(endDateStr, "date");
            LocalDate currentDate = startDate;

            while (!currentDate.isAfter(endDate)) {
                String key = currentDate.toString();
                appointmentStatistics.put(key, BigDecimal.ZERO); // Đảm bảo trả về tất cả các mốc ngày trong khoảng thời gian
                currentDate = currentDate.plusDays(1);
            }
        }

        // Kiểm tra nếu không có spec, thay thế bằng null
        if (spec == null) {
            spec = Specification.where(null);
        }

        // Lấy danh sách cuộc hẹn
        List<AppointmentBuilding> appointments = appointmentBuildingRepository.findAll(spec);

        // Dữ liệu thống kê cuối cùng
        Map<String, Object> statistics = new HashMap<>(appointmentStatistics);

        // Nhóm các cuộc hẹn theo thời gian và tính tổng
        appointments.stream()
                .collect(groupingBy(appointment -> {
                    LocalDateTime createdAt = appointment.getVisitTime(); // Sử dụng visitTime thay vì createdAt
                    return switch (type) {
                        case "month", "quarter", "date" -> createdAt.toLocalDate().toString();
                        case "year" -> {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
                            yield createdAt.format(formatter);
                        }
                        default -> throw new IllegalArgumentException("Invalid date type: " + type);
                    };
                }, counting()))
                .forEach((key, count) -> statistics.put(key, count));

        return statistics;
    }

    private Specification<AppointmentBuilding> getAppointmentStatisticSpec(Map<String, String> params) {
        Specification<AppointmentBuilding> spec = Specification.where(null);

        if (params.containsKey("startDate") && params.containsKey("type")) {
            String visitTimeStr = params.get("startDate");
            String type = params.get("type");
            LocalDate visitTime = dateUtils.parseDate(visitTimeStr, type);
            spec = spec.and((root, query, cb) -> {
                LocalDate endDate;
                if (type.equalsIgnoreCase("date")) {
                    endDate = visitTime.plusDays(1);
                } else if (type.equalsIgnoreCase("month")) {
                    endDate = visitTime.withDayOfMonth(visitTime.lengthOfMonth()).plusDays(1);
                } else if (type.equalsIgnoreCase("quarter")) {
                    endDate = visitTime.plusMonths(3).withDayOfMonth(1).minusDays(1).plusDays(1);
                } else if (type.equalsIgnoreCase("year")) {
                    endDate = visitTime.withDayOfYear(visitTime.lengthOfYear()).plusDays(1);
                } else {
                    throw new IllegalArgumentException("Invalid date type: " + type);
                }
                return cb.between(root.get("createdAt"), visitTime.atStartOfDay(), endDate.atStartOfDay());
            });
        }

        if (params.containsKey("startDate") && params.containsKey("endDate")) {
            String visitTimeStr = params.get("startDate");
            LocalDate visitTime = dateUtils.parseDate(visitTimeStr, "date");
            String endVisitTimeStr = params.get("endDate");
            LocalDate endVisitTime = dateUtils.parseDate(endVisitTimeStr, "date").plusDays(1);
            spec = spec.and((root, query, cb) -> cb.between(root.get("createdAt"), visitTime.atStartOfDay(), endVisitTime.atStartOfDay()));

        }

        return spec;

    }

    private Specification<AppointmentBuilding> getAppointmentStatisticSpecTime(Map<String, String> params) {
        Specification<AppointmentBuilding> spec = Specification.where(null);

        if (params.containsKey("startDate") && params.containsKey("type")) {
            String visitTimeStr = params.get("startDate");
            String type = params.get("type");
            LocalDate visitTime = dateUtils.parseDate(visitTimeStr, type);
            spec = spec.and((root, query, cb) -> {
                LocalDate endDate;
                if (type.equalsIgnoreCase("date")) {
                    endDate = visitTime.plusDays(1);
                } else if (type.equalsIgnoreCase("month")) {
                    endDate = visitTime.withDayOfMonth(visitTime.lengthOfMonth()).plusDays(1);
                } else if (type.equalsIgnoreCase("quarter")) {
                    endDate = visitTime.plusMonths(3).withDayOfMonth(1).minusDays(1).plusDays(1);
                } else if (type.equalsIgnoreCase("year")) {
                    endDate = visitTime.withDayOfYear(visitTime.lengthOfYear()).plusDays(1);
                } else {
                    throw new IllegalArgumentException("Invalid date type: " + type);
                }
                return cb.between(root.get("visitTime"), visitTime.atStartOfDay(), endDate.atStartOfDay());
            });
        }

        if (params.containsKey("startDate") && params.containsKey("endDate")) {
            String visitTimeStr = params.get("startDate");
            LocalDate visitTime = dateUtils.parseDate(visitTimeStr, "date");
            String endVisitTimeStr = params.get("endDate");
            LocalDate endVisitTime = dateUtils.parseDate(endVisitTimeStr, "date").plusDays(1);
            spec = spec.and((root, query, cb) -> cb.between(root.get("visitTime"), visitTime.atStartOfDay(), endVisitTime.atStartOfDay()));

        }
        return spec;
    }


    private Specification<AppointmentBuilding> getBuildingSpec(Map<String, String> params) {
        Specification<AppointmentBuilding> spec = Specification.where(null);

        if (params.containsKey("status")) {
            String buildingStatus = params.get("status").trim().toUpperCase();
            spec = spec.and((root, query, cb) -> {
                Join<AppointmentBuilding, AppointmentBuildingStatusHistory> statusHistoryJoin = root.join("appointmentBuildingStatusHistories", JoinType.INNER);
                return cb.equal(statusHistoryJoin.get("status"), buildingStatus);
            });
        }

        if (params.containsKey("startDate") && params.containsKey("type")) {
            String visitTimeStr = params.get("startDate");
            String type = params.get("type");
            LocalDate visitTime = dateUtils.parseDate(visitTimeStr, type);
            spec = spec.and((root, query, cb) -> {
                LocalDate endDate;
                if (type.equalsIgnoreCase("date")) {
                    endDate = visitTime.plusDays(1);
                } else if (type.equalsIgnoreCase("month")) {
                    endDate = visitTime.withDayOfMonth(visitTime.lengthOfMonth()).plusDays(1);
                } else if (type.equalsIgnoreCase("quarter")) {
                    endDate = visitTime.plusMonths(3).withDayOfMonth(1).minusDays(1).plusDays(1);
                } else if (type.equalsIgnoreCase("year")) {
                    endDate = visitTime.withDayOfYear(visitTime.lengthOfYear()).plusDays(1);
                } else {
                    throw new IllegalArgumentException("Invalid date type: " + type);
                }
                return cb.between(root.get("visitTime"), visitTime.atStartOfDay(), endDate.atStartOfDay());
            });
        }

        if (params.containsKey("startDate") && params.containsKey("endDate")) {
            String visitTimeStr = params.get("startDate");
            LocalDate visitTime = dateUtils.parseDate(visitTimeStr, "date");
            String endVisitTimeStr = params.get("endDate");
            LocalDate endVisitTime = dateUtils.parseDate(endVisitTimeStr, "date").plusDays(1);
            spec = spec.and((root, query, cb) -> cb.between(root.get("visitTime"), visitTime.atStartOfDay(), endVisitTime.atStartOfDay()));
        }

        if (params.containsKey("email")) {
            String email = params.get("email").trim();
            spec = spec.and((root, query, cb) -> {
                Join<AppointmentBuilding, Appointment> appointmentJoin = root.join("appointment", JoinType.INNER);
                Join<Appointment, Customer> customerJoin = appointmentJoin.join("customer", JoinType.INNER);
                return cb.equal(customerJoin.get("email"), email);
            });
        }

        return spec;
    }


}