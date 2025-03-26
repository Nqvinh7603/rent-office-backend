/*******************************************************************************
 * Class        ：AppointmentServiceImpl
 * Created date ：2025/03/25
 * Lasted date  ：2025/03/25
 * Author       ：vinhNQ2
 * Change log   ：2025/03/25：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.service.impl;

import com.nqvinh.rentofficebackend.application.exception.ResourceNotFoundException;
import com.nqvinh.rentofficebackend.domain.building.constant.AppointmentBuildingStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.AppointmentStatus;
import com.nqvinh.rentofficebackend.domain.building.constant.RequireTypeEnum;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.AppointmentBuildingReqDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.AppointmentReqDto;
import com.nqvinh.rentofficebackend.domain.building.dto.request.appointment.request.CustomerAppointmentReqDto;
import com.nqvinh.rentofficebackend.domain.building.entity.*;
import com.nqvinh.rentofficebackend.domain.building.repository.BuildingRepository;
import com.nqvinh.rentofficebackend.domain.building.repository.CustomerRepository;
import com.nqvinh.rentofficebackend.domain.building.service.AppointmentService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
                        .note(appointmentReq.getNote())
                        .appointments(new ArrayList<>())
                        .build());

        if (appointmentReq.getAppointments() != null) {
            for (AppointmentReqDto appointmentReqDto : appointmentReq.getAppointments()) {
                Appointment appointment = Appointment.builder()
                        .customer(customer)
                        .appointmentBuildings(new ArrayList<>())
                        .appointmentStatusHistories(new ArrayList<>())
                        .build();

                AppointmentStatusHistory appointmentStatusHistory = AppointmentStatusHistory.builder()
                        .appointment(appointment)
                        .status(AppointmentStatus.PENDING)
                        .build();
                appointment.getAppointmentStatusHistories().add(appointmentStatusHistory);

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

}