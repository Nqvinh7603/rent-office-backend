package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.domain.building.constant.AppointmentBuildingStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.ConsignmentStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.PotentialCustomerStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.RequireTypeEnum;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.CustomerPotentialDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.*;
import com.nqvinh.rentofficebackend.domain.building.mapper.CustomerMapper;
import com.nqvinh.rentofficebackend.domain.building.mapper.appointment.request.CustomerPotentialMapper;
import com.nqvinh.rentofficebackend.domain.building.mapper.request.CustomerReqMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.AppointmentRepository;
import com.nqvinh.rentofficebackend.domain.building.repository.CustomerRepository;
import com.nqvinh.rentofficebackend.domain.building.service.CustomerService;
import com.nqvinh.rentofficebackend.domain.common.constant.MailStatus;
import com.nqvinh.rentofficebackend.domain.common.constant.MailType;
import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;
import com.nqvinh.rentofficebackend.domain.common.service.EmailProducer;
import com.nqvinh.rentofficebackend.domain.common.service.NotificationService;
import com.nqvinh.rentofficebackend.infrastructure.utils.DateUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.StringUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    UserService userService;
    UserRepository userRepository;
    UserMapper userMapper;
    CustomerMapper customerMapper;
    CustomerReqMapper customerReqMapper;
    NotificationService notificationService;
    EmailProducer emailProducer;
    StringUtils stringUtils;
    PaginationUtils paginationUtils;
    CustomerPotentialMapper customerPotentialMapper;
    DateUtils dateUtils;
    private final AppointmentRepository appointmentRepository;

    @Override
    @SneakyThrows
    @Transactional
    public void assignmentCustomerToStaffs(AssignCustomerDto assignCustomerDto) {
        Customer customer = customerRepository.findById(assignCustomerDto.getCustomer().getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        List<User> users = assignCustomerDto.getUsers().stream()
                .map(userDto -> userRepository.findById(userDto.getUserId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found")))
                .collect(Collectors.toList());
        customer.setUsers(userService.getUsersByIdIn(userMapper.toDtoList(users)));
        notificationService.assignCustomerToStaffs(assignCustomerDto);
        customerRepository.save(customer);
    }

    @Override
    public List<UserDto> getStaffsByCustomerId(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        List<UserDto> userDto = userService.loadStaff();
        return userDto.stream()
                .peek(user -> {
                    if (customer.getUsers().stream().anyMatch(customerUser -> customerUser.getUserId().equals(user.getUserId()))) {
                        user.setChecked("checked");
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    @SneakyThrows
    public List<CustomerDto> getCustomersByRequireType() {
        UserDto currentUser = userService.getLoggedInUser();

        if ("ADMIN".equals(currentUser.getRole().getRoleName()) || "MANAGER".equals(currentUser.getRole().getRoleName())) {
            return customerRepository.findByRequireType(RequireTypeEnum.CONSIGNMENT)
                    .stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
        }

        return customerRepository.findCustomersByRequireTypeAndUser(RequireTypeEnum.CONSIGNMENT, currentUser.getUserId())
                .stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Customer findOrCreateCustomer(CustomerReqDto customerReqDto) {
        return customerRepository.findByEmail(customerReqDto.getEmail())
                .orElseGet(() -> {
                    Customer newCustomer = customerReqMapper.toEntity(customerReqDto);
                    newCustomer.setBuildings(new ArrayList<>());
                    return newCustomer;
                });
    }

    @Override
    @Transactional
    public CustomerDto createPotentialCustomer(CustomerDto customerDto) {
        Customer existingCustomer = customerRepository.findByEmail(customerDto.getEmail()).orElse(null);
        List<UserDto> adminsAndManagers = userService.getAllAdminsAndManagers();
        adminsAndManagers.forEach(adminOrManager -> notificationService.createPotentialCustomerNotification(adminOrManager, customerDto));

        sendMailForPotentialCustomer(customerDto);
        if (existingCustomer != null) {
            existingCustomer.setNote(customerDto.getNote());
            existingCustomer.setStatus(PotentialCustomerStatus.valueOf(customerDto.getStatus()));
            return customerMapper.toDto(customerRepository.save(existingCustomer));
        }


        Customer customer = customerMapper.toEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toDto(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerPotentialDto updatePotentialCustomer(Long customerId, CustomerPotentialDto customerDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        List<Appointment> existingAppointments = customer.getAppointments();

        List<Appointment> appointmentsToRemove = new ArrayList<>();
        for (Appointment existingAppointment : existingAppointments) {
            boolean isAppointmentExistInDto = customerDto.getAppointments().stream()
                    .anyMatch(appointmentDto -> appointmentDto.getAppointmentId().equals(existingAppointment.getAppointmentId()));
            if (!isAppointmentExistInDto) {
                appointmentsToRemove.add(existingAppointment);
            }
        }
        customer.getAppointments().removeAll(appointmentsToRemove);
        appointmentsToRemove.forEach(appointment -> appointment.setCustomer(null));
        appointmentRepository.deleteAll(appointmentsToRemove);

        customerDto.getAppointments().forEach(appointmentDto -> {
            Appointment appointment = customer.getAppointments().stream()
                    .filter(existingAppointment -> existingAppointment.getAppointmentId() != null && Objects.equals(existingAppointment.getAppointmentId(), appointmentDto.getAppointmentId()))
                    .findFirst()
                    .orElseGet(() -> {
                        Appointment newAppointment = Appointment.builder()
                                .appointmentBuildings(new ArrayList<>())
                                .customer(customer)
                                .build();
                        customer.getAppointments().add(newAppointment);
                        return newAppointment;
                    });

            appointment.getAppointmentBuildings().clear();

            appointmentDto.getAppointmentBuildings().forEach(appointmentBuildingDto -> {
                AppointmentBuilding appointmentBuilding = AppointmentBuilding.builder()
                        .appointmentBuildingId(appointmentBuildingDto.getAppointmentBuildingId())
                        .appointment(appointment)
                        .building(Building.builder().buildingId(appointmentBuildingDto.getBuilding().getBuildingId()).build())
                        .visitTime(appointmentBuildingDto.getVisitTime())
                        .area(appointmentBuildingDto.getArea())
                        .appointmentBuildingStatusHistories(new ArrayList<>())
                        .build();

                appointment.getAppointmentBuildings().add(appointmentBuilding);

                appointmentBuilding.setAppointmentBuildingStatusHistories(Collections.singletonList(
                        AppointmentBuildingStatusHistory.builder()
                                .status(AppointmentBuildingStatus.PENDING)
                                .appointmentBuilding(appointmentBuilding)
                                .build()

                ));

                appointment.getAppointmentBuildings().add(appointmentBuilding);
            });
        });

        customer.setAppointments(existingAppointments);
        customerPotentialMapper.partialUpdate(customer, customerDto);
        customer.getAppointments().forEach(appointment -> appointment.setCustomer(customer));
        return customerPotentialMapper.toDto(customerRepository.save(customer));
    }


    @Override
    public void deletePotentialCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    private boolean isAdmin(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        String roleName = user.getRole().getRoleName();
        return roleName.equals("ADMIN") || roleName.equals("MANAGER");
    }

    @Override
    @SneakyThrows
    public Page<CustomerPotentialDto> getPotentialCustomers(Map<String, String> params) {
        Specification<Customer> spec = getCustomerSpec(params);
        UserDto currentUser = userService.getLoggedInUser();

        if (!isAdmin(currentUser)) {
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, User> customerJoin = root.join("users", JoinType.INNER);
                return criteriaBuilder.equal(customerJoin.get("userId"), currentUser.getUserId());
            });
        }
        Pageable pageable = paginationUtils.buildPageable(params);


        org.springframework.data.domain.Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(customerPage, pageable);
        return paginationUtils.mapPage(customerPage, meta, customerPotentialMapper::toDto);
    }

    @Override
    public List<CustomerDto> getPotentialCustomers() {
        return customerMapper.toDtoList(customerRepository.findPotentialCustomer());
    }

    @Override
    public CustomerPotentialDto getPotentialCustomersById(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customerPotentialMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    @Override
    @SneakyThrows
    public List<CustomerDto> getAllCustomers() {
        UserDto currentUser = userService.getLoggedInUser();
        boolean isAdminOrManager = "ADMIN".equals(currentUser.getRole().getRoleName()) || "MANAGER".equals(currentUser.getRole().getRoleName());
        if (isAdminOrManager) {
            return customerMapper.toDtoList(customerRepository.findAll());
        } else {
            return customerMapper.toDtoList(customerRepository.findAllCustomers(currentUser.getUserId(), isAdminOrManager));
        }
        //return customerMapper.toDtoList(customerRepository.findAll());
    }

    @Override
    public Map<String, Object> getCustomerStatistics(Map<String, String> params) {
        Map<String, Object> statistics = new HashMap<>();
        List<Customer> customers = customerRepository.findAll(getCustomerStatisticSpec(params));

        statistics.put("totalCustomers", (long) customers.size());
        statistics.put("potentialCustomers", customers.stream().filter(customer -> customer.getStatus() != null && customer.getRequireType() == RequireTypeEnum.RENT).count());
        statistics.put("consignmentCustomers", customers.stream().filter(customer -> customer.getRequireType() == RequireTypeEnum.CONSIGNMENT).count());


        //thống kê theo trạng thái khách hàng thuê
        statistics.put("NOT_CONTACTED_RENT_CUSTOMER", customers.stream().filter(customer -> customer.getStatus() == PotentialCustomerStatus.NOT_CONTACTED || customer.getRequireType() == RequireTypeEnum.RENT).count());
        statistics.put("CONTACTED_RENT_CUSTOMER", customers.stream().filter(customer -> customer.getStatus() == PotentialCustomerStatus.CONTACTED || customer.getRequireType() == RequireTypeEnum.RENT).count());
        statistics.put("DEAL_IN_PROGRESS_RENT_CUSTOMER", customers.stream().filter(customer -> customer.getStatus() == PotentialCustomerStatus.DEAL_IN_PROGRESS || customer.getRequireType() == RequireTypeEnum.RENT).count());
        statistics.put("DEAL_DONE_RENT_CUSTOMER", customers.stream().filter(customer -> customer.getStatus() == PotentialCustomerStatus.DEAL_DONE || customer.getRequireType() == RequireTypeEnum.RENT).count());
        statistics.put("CANCELED_RENT_CUSTOMER", customers.stream().filter(customer -> customer.getStatus() == PotentialCustomerStatus.CANCELED || customer.getRequireType() == RequireTypeEnum.RENT).count());

        //thống kê theo trạng thái khách hàng ký gửi
        statistics.put("PENDING_CONSIGNMENT_CUSTOMER", customers.stream()
                .filter(customer -> customer.getRequireType() == RequireTypeEnum.CONSIGNMENT && customer.getBuildings().stream()
                        .anyMatch(building -> {
                            ConsignmentStatusHistory lastHistory = building.getConsignmentStatusHistories()
                                    .stream()
                                    .reduce((first, second) -> second)
                                    .orElse(null);
                            return lastHistory != null && lastHistory.getStatus() == ConsignmentStatus.PENDING;
                        })
                ).count());

        statistics.put("CONFIRMED_CONSIGNMENT_CUSTOMER", customers.stream()
                .filter(customer -> customer.getRequireType() == RequireTypeEnum.CONSIGNMENT && customer.getBuildings().stream()
                        .anyMatch(building -> {
                            ConsignmentStatusHistory lastHistory = building.getConsignmentStatusHistories()
                                    .stream()
                                    .reduce((first, second) -> second)
                                    .orElse(null);
                            return lastHistory != null && lastHistory.getStatus() == ConsignmentStatus.CONFIRMED;
                        })
                ).count());

        statistics.put("CANCELLED_CONSIGNMENT_CUSTOMER", customers.stream()
                .filter(customer -> customer.getRequireType() == RequireTypeEnum.CONSIGNMENT && customer.getBuildings().stream()
                        .anyMatch(building -> {
                            ConsignmentStatusHistory lastHistory = building.getConsignmentStatusHistories()
                                    .stream()
                                    .reduce((first, second) -> second)
                                    .orElse(null);
                            return lastHistory != null && lastHistory.getStatus() == ConsignmentStatus.CANCELLED;
                        })
                ).count());

        statistics.put("INCOMPLETE_CONSIGNMENT_CUSTOMER", customers.stream()
                .filter(customer -> customer.getRequireType() == RequireTypeEnum.CONSIGNMENT && customer.getBuildings().stream()
                        .anyMatch(building -> {
                            ConsignmentStatusHistory lastHistory = building.getConsignmentStatusHistories()
                                    .stream()
                                    .reduce((first, second) -> second)
                                    .orElse(null);
                            return lastHistory != null && lastHistory.getStatus() == ConsignmentStatus.INCOMPLETE;
                        })
                ).count());

        statistics.put("ADDITIONAL_INFO_CONSIGNMENT_CUSTOMER", customers.stream()
                .filter(customer -> customer.getRequireType() == RequireTypeEnum.CONSIGNMENT && customer.getBuildings().stream()
                        .anyMatch(building -> {
                            ConsignmentStatusHistory lastHistory = building.getConsignmentStatusHistories()
                                    .stream()
                                    .reduce((first, second) -> second)
                                    .orElse(null);
                            return lastHistory != null && lastHistory.getStatus() == ConsignmentStatus.ADDITIONAL_INFO;
                        })
                ).count());

        return statistics;
    }

    @Override
    public Map<String, Object> getCustomerStatisticsByTime(Map<String, String> params) {
        Specification<Customer> spec = getCustomerStatisticSpec(params);
        String type = params.getOrDefault("type", "");
        String startDateStr = params.getOrDefault("startDate", null);
        String endDateStr = params.getOrDefault("endDate", null);

        Map<String, BigDecimal> appointmentStatistics = new TreeMap<>();
        if (type.equals("year")) {
            int currentYear = LocalDate.now().getYear();
            IntStream.rangeClosed(1, 12).forEach(month -> {
                String key = YearMonth.of(currentYear, month).toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
            });
        }
        if (type.equals("month")) {
            LocalDate now = LocalDate.now();
            int currentMonth = now.getMonthValue();
            int currentDateOfMonth;
            int requestedMonth = Integer.parseInt(startDateStr.split("-")[1]);
            YearMonth yearMonth = YearMonth.of(now.getYear(), requestedMonth);
            if (currentMonth == requestedMonth) {
                currentDateOfMonth = now.getDayOfMonth();
            } else {
                currentDateOfMonth = yearMonth.lengthOfMonth();
            }
            IntStream.rangeClosed(1, currentDateOfMonth).forEach(day -> {
                String key = yearMonth.atDay(day).toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
            });
        }

        if (type.equals("quarter")) {
            LocalDate startDate = dateUtils.parseDate(startDateStr, "quarter");
            LocalDate endDate = startDate.plusMonths(3).withDayOfMonth(1).minusDays(1);
            while (!startDate.isAfter(endDate)) {
                String key = startDate.toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
                startDate = startDate.plusDays(1);
            }
        }

        if (startDateStr != null && endDateStr != null && type.equals("date")) {
            LocalDate startDate = dateUtils.parseDate(startDateStr, "date");
            LocalDate endDate = dateUtils.parseDate(endDateStr, "date");
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                String key = currentDate.toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
                currentDate = currentDate.plusDays(1);
            }
        }

        if (spec == null) {
            spec = Specification.where(null);
        }


        List<Customer> appointments = customerRepository.findAll(spec);

        Map<String, Object> statistics = new HashMap<>(appointmentStatistics);

        appointments.stream()
                .collect(groupingBy(appointment -> {
                    LocalDateTime createdAt = appointment.getCreatedAt();
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

    @Override
    public Map<String, Object> getCustomerStatisticsByTimeAndTypeConsignment(Map<String, String> params) {
        Specification<Customer> spec = getCustomerStatisticSpec(params);
        String type = params.getOrDefault("type", "");
        String startDateStr = params.getOrDefault("startDate", null);
        String endDateStr = params.getOrDefault("endDate", null);

        Map<String, BigDecimal> appointmentStatistics = new TreeMap<>();
        if (type.equals("year")) {
            int currentYear = LocalDate.now().getYear();
            IntStream.rangeClosed(1, 12).forEach(month -> {
                String key = YearMonth.of(currentYear, month).toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
            });
        }
        if (type.equals("month")) {
            LocalDate now = LocalDate.now();
            int currentMonth = now.getMonthValue();
            int currentDateOfMonth;
            int requestedMonth = Integer.parseInt(startDateStr.split("-")[1]);
            YearMonth yearMonth = YearMonth.of(now.getYear(), requestedMonth);
            if (currentMonth == requestedMonth) {
                currentDateOfMonth = now.getDayOfMonth();
            } else {
                currentDateOfMonth = yearMonth.lengthOfMonth();
            }
            IntStream.rangeClosed(1, currentDateOfMonth).forEach(day -> {
                String key = yearMonth.atDay(day).toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
            });
        }

        if (type.equals("quarter")) {
            LocalDate startDate = dateUtils.parseDate(startDateStr, "quarter");
            LocalDate endDate = startDate.plusMonths(3).withDayOfMonth(1).minusDays(1);
            while (!startDate.isAfter(endDate)) {
                String key = startDate.toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
                startDate = startDate.plusDays(1);
            }
        }

        if (startDateStr != null && endDateStr != null && type.equals("date")) {
            LocalDate startDate = dateUtils.parseDate(startDateStr, "date");
            LocalDate endDate = dateUtils.parseDate(endDateStr, "date");
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                String key = currentDate.toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
                currentDate = currentDate.plusDays(1);
            }
        }

        if (spec == null) {
            spec = Specification.where(null);
        }


        List<Customer> appointments = customerRepository.findAll(spec);

        Map<String, Object> statistics = new HashMap<>(appointmentStatistics);

        appointments.stream()
                .filter(appointment -> appointment.getRequireType() == RequireTypeEnum.CONSIGNMENT)
                .collect(groupingBy(appointment -> {
                    LocalDateTime createdAt = appointment.getCreatedAt();
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

    @Override
    public Map<String, Object> getCustomerStatisticsByTimeAndTypePotential(Map<String, String> params) {
        Specification<Customer> spec = getCustomerStatisticSpec(params);
        String type = params.getOrDefault("type", "");
        String startDateStr = params.getOrDefault("startDate", null);
        String endDateStr = params.getOrDefault("endDate", null);

        Map<String, BigDecimal> appointmentStatistics = new TreeMap<>();
        if (type.equals("year")) {
            int currentYear = LocalDate.now().getYear();
            IntStream.rangeClosed(1, 12).forEach(month -> {
                String key = YearMonth.of(currentYear, month).toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
            });
        }
        if (type.equals("month")) {
            LocalDate now = LocalDate.now();
            int currentMonth = now.getMonthValue();
            int currentDateOfMonth;
            int requestedMonth = Integer.parseInt(startDateStr.split("-")[1]);
            YearMonth yearMonth = YearMonth.of(now.getYear(), requestedMonth);
            if (currentMonth == requestedMonth) {
                currentDateOfMonth = now.getDayOfMonth();
            } else {
                currentDateOfMonth = yearMonth.lengthOfMonth();
            }
            IntStream.rangeClosed(1, currentDateOfMonth).forEach(day -> {
                String key = yearMonth.atDay(day).toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
            });
        }

        if (type.equals("quarter")) {
            LocalDate startDate = dateUtils.parseDate(startDateStr, "quarter");
            LocalDate endDate = startDate.plusMonths(3).withDayOfMonth(1).minusDays(1);
            while (!startDate.isAfter(endDate)) {
                String key = startDate.toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
                startDate = startDate.plusDays(1);
            }
        }

        if (startDateStr != null && endDateStr != null && type.equals("date")) {
            LocalDate startDate = dateUtils.parseDate(startDateStr, "date");
            LocalDate endDate = dateUtils.parseDate(endDateStr, "date");
            LocalDate currentDate = startDate;
            while (!currentDate.isAfter(endDate)) {
                String key = currentDate.toString();
                appointmentStatistics.put(key, BigDecimal.ZERO);
                currentDate = currentDate.plusDays(1);
            }
        }

        if (spec == null) {
            spec = Specification.where(null);
        }


        List<Customer> appointments = customerRepository.findAll(spec);

        Map<String, Object> statistics = new HashMap<>(appointmentStatistics);

        appointments.stream()
                .filter(appointment -> appointment.getRequireType() == RequireTypeEnum.RENT && appointment.getStatus() != null) // Chỉ lấy customer rent
                .collect(groupingBy(appointment -> {
                    LocalDateTime createdAt = appointment.getCreatedAt();
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


    private Specification<Customer> getCustomerStatisticSpec(Map<String, String> params) {
        Specification<Customer> spec = Specification.where(null);

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

    private Specification<Customer> getCustomerSpec(Map<String, String> params) {
        Specification<Customer> spec = (root, query, cb) -> cb.isNotNull(root.get("status"));

        if (params.containsKey("staffName")) {
            String staffName = stringUtils.normalizeString(params.get("staffName").trim().toLowerCase());
            String[] nameParts = staffName.split("\\s+");
            String likePatternFirstName = nameParts.length > 1
                    ? "%" + String.join(" ", Arrays.copyOfRange(nameParts, 0, nameParts.length - 1)) + "%"
                    : "%" + nameParts[0] + "%";
            String likePatternLastName = "%" + nameParts[nameParts.length - 1] + "%";
            spec = spec.and((root, query, criteriaBuilder) -> {
                Join<Customer, User> userJoin = root.join("users", JoinType.INNER);
                return criteriaBuilder.and(
                        criteriaBuilder.isNotNull(userJoin.get("userId")),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(userJoin.get("firstName"))), likePatternFirstName),
                        criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(userJoin.get("lastName"))), likePatternLastName)
                );
            });
        }

        if (params.containsKey("email")) {
            String ward = stringUtils.normalizeString(params.get("email").trim().toLowerCase());
            String likePattern = "%" + ward + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("email"))), likePattern)
            );
        }

        if (params.containsKey("phoneNumber")) {
            String phoneNumber = stringUtils.normalizeString(params.get("phoneNumber").trim().toLowerCase());
            String likePattern = "%" + phoneNumber + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("phoneNumber"))), likePattern)
            );
        }

        if (params.containsKey("customerName")) {
            String customerName = stringUtils.normalizeString(params.get("customerName").trim().toLowerCase());
            String likePattern = "%" + customerName + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("customerName"))), likePattern)
            );
        }


        if (params.containsKey("status")) {
            String requireType = params.get("status");
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), requireType));
        }
        return spec;
    }


    private void sendMailForPotentialCustomer(CustomerDto customerDto) {
        var mailNewPotentialCustomer = MailEvent.builder()
                .toAddress(customerDto.getEmail())
                .subject("Xác nhận yêu cầu tư vấn từ hệ thống")
                .templateName("new-potential-customer")
                .context(
                        Map.of(
                                "customerName", customerDto.getCustomerName(),
                                "note", customerDto.getNote(),
                                "phoneNumber", customerDto.getPhoneNumber()
                        )
                )
                .status(MailStatus.INIT.getStatus())
                .code(MessageCode.MAIL_CREATE_CUSTOMER_POTENTIAL.getCode())
                .type(MailType.CREATE_CUSTOMER_POTENTIAL.getType())
                .build();

        emailProducer.sendMailNewPotentialCustomer(mailNewPotentialCustomer);
    }

}