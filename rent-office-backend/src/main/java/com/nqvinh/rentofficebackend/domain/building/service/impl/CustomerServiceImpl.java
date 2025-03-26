package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.dto.response.Meta;
import com.nqvinh.rentofficebackend.application.dto.response.Page;
import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.domain.building.constant.PotentialCustomerStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.RequireTypeEnum;
import com.nqvinh.rentofficebackend.domain.building.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Customer;
import com.nqvinh.rentofficebackend.domain.building.mapper.CustomerMapper;
import com.nqvinh.rentofficebackend.domain.building.mapper.request.CustomerReqMapper;
import com.nqvinh.rentofficebackend.domain.building.repository.CustomerRepository;
import com.nqvinh.rentofficebackend.domain.building.service.CustomerService;
import com.nqvinh.rentofficebackend.domain.common.constant.MailStatus;
import com.nqvinh.rentofficebackend.domain.common.constant.MailType;
import com.nqvinh.rentofficebackend.domain.common.constant.MessageCode;
import com.nqvinh.rentofficebackend.domain.common.event.MailEvent;
import com.nqvinh.rentofficebackend.domain.common.service.EmailProducer;
import com.nqvinh.rentofficebackend.domain.common.service.NotificationService;
import com.nqvinh.rentofficebackend.infrastructure.utils.PaginationUtils;
import com.nqvinh.rentofficebackend.infrastructure.utils.StringUtils;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public CustomerDto updatePotentialCustomer(Long customerId, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerMapper.partialUpdate(customer, customerDto);
        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    public void deletePotentialCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public Page<CustomerDto> getPotentialCustomers(Map<String, String> params) {
        Specification<Customer> spec = getCustomerSpec(params);
        Pageable pageable = paginationUtils.buildPageable(params);
        org.springframework.data.domain.Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        Meta meta = paginationUtils.buildMeta(customerPage, pageable);
        return paginationUtils.mapPage(customerPage, meta, customerMapper::toDto);
    }

    @Override
    public List<CustomerDto> getPotentialCustomers() {
        return customerMapper.toDtoList(customerRepository.findPotentialCustomer());
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

        if(params.containsKey("phoneNumber")) {
            String phoneNumber = stringUtils.normalizeString(params.get("phoneNumber").trim().toLowerCase());
            String likePattern = "%" + phoneNumber + "%";
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("phoneNumber"))), likePattern)
            );
        }

        if(params.containsKey("customerName")) {
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