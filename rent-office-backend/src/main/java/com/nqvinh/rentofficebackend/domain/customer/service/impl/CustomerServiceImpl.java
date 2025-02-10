package com.nqvinh.rentofficebackend.domain.customer.service.impl;

import com.nqvinh.rentofficebackend.domain.auth.dto.UserDto;
import com.nqvinh.rentofficebackend.domain.auth.entity.User;
import com.nqvinh.rentofficebackend.domain.auth.mapper.UserMapper;
import com.nqvinh.rentofficebackend.domain.auth.repository.UserRepository;
import com.nqvinh.rentofficebackend.domain.auth.service.UserService;
import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import com.nqvinh.rentofficebackend.domain.customer.dto.AssignCustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.ConsignmentImageDto;
import com.nqvinh.rentofficebackend.domain.customer.dto.CustomerDto;
import com.nqvinh.rentofficebackend.domain.customer.entity.Consignment;
import com.nqvinh.rentofficebackend.domain.customer.entity.ConsignmentImage;
import com.nqvinh.rentofficebackend.domain.customer.entity.Customer;
import com.nqvinh.rentofficebackend.domain.customer.mapper.ConsignmentMapper;
import com.nqvinh.rentofficebackend.domain.customer.mapper.CustomerMapper;
import com.nqvinh.rentofficebackend.domain.customer.repository.CustomerRepository;
import com.nqvinh.rentofficebackend.domain.customer.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    ImageService imageService;
    UserService userService;
    UserRepository userRepository;
    UserMapper userMapper;
    ConsignmentMapper consignmentMapper;

    @Override
    @SneakyThrows
    @Transactional
    public CustomerDto createCustomerWithConsignment(CustomerDto customerDto, List<MultipartFile> consignmentImages) {
        Customer customer = customerRepository.findByEmail(customerDto.getEmail())
                .orElseGet(() -> customerMapper.toEntity(customerDto));

        List<String> uploadedUrls = imageService.handleImageUpload(consignmentImages, customerDto.getConsignments().stream()
                .flatMap(consignment -> consignment.getConsignmentImages().stream())
                .map(ConsignmentImageDto::getImgUrl)
                .collect(Collectors.toList())).get();

        List<Consignment> newConsignments = customerDto.getConsignments().stream()
                .map(consignmentDto -> {
                    Consignment newConsignment = consignmentMapper.toEntity(consignmentDto);
                    newConsignment.setCustomer(customer);
                    List<ConsignmentImage> consignmentImageEntities = uploadedUrls.stream()
                            .map(imgUrl -> ConsignmentImage.builder().imgUrl(imgUrl).consignment(newConsignment).build())
                            .collect(Collectors.toList());
                    newConsignment.setConsignmentImages(consignmentImageEntities);
                    return newConsignment;
                })
                .collect(Collectors.toList());

        customer.getConsignments().addAll(newConsignments);
        return customerMapper.toDto(customerRepository.save(customer));
    }

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


}

