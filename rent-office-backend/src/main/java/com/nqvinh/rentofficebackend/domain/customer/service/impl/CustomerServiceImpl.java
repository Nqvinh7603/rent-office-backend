package com.nqvinh.rentofficebackend.domain.customer.service.impl;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.domain.customer.constant.RequireTypeEnum;
import com.nqvinh.rentofficebackend.domain.customer.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.request.CustomerReqDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import com.nqvinh.rentofficebackend.domain.customer.mapper.CustomerMapper;
import com.nqvinh.rentofficebackend.domain.customer.mapper.request.CustomerReqMapper;
import com.nqvinh.rentofficebackend.domain.customer.repository.CustomerRepository;
import com.nqvinh.rentofficebackend.domain.customer.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
                    newCustomer.setConsignments(new ArrayList<>());
                    return newCustomer;
                });
    }


}