/*******************************************************************************
 * Class        ：BuildingClientRes
 * Created date ：2025/03/20
 * Lasted date  ：2025/03/20
 * Author       ：vinhNQ2
 * Change log   ：2025/03/20：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.dto.response.client;

import com.nqvinh.rentofficebackend.domain.building.dto.*;
import com.nqvinh.rentofficebackend.domain.building.dto.request.RentalPricingReqDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

/**
 * BuildingClientRes
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuildingClientRes {
    Long buildingId;

    @NotBlank(message = "Consignment name is required")
    String ward; // phường

    @NotBlank(message = "District is required")
    String district; // quận

    @NotBlank(message = "City is required")
    String city; // thành phố

    @NotBlank(message = "Street is required")
    String street; // địa chỉ

    @NotBlank(message = "Orientation is required")
    String orientation; //Hướng

    @NotNull(message = "List image is required")
    List<BuildingImageDto> buildingImages = List.of();

    @NotNull(message = "Rental pricing is required")
    List<RentalPricingReqDto> rentalPricing;

    @NotBlank(message = "Description is required")
    String description;

    @NotNull(message = "Building type is required")
    BuildingTypeDto buildingType;

    String buildingNumber;

    @NotNull(message = "Status is required")
    List<ConsignmentStatusHistoryDto> consignmentStatusHistories = List.of();

    String buildingStatus;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;

    @NotNull(message = "Customer is required")
    CustomerDto customer;

    @NotBlank(message = "Building name is required")
    String buildingName;

    @NotNull(message = "Fee is required")
    List<FeeDto> fees;

    @NotBlank(message = "Number of floor is required")
    Integer numberOfFloors;

    @NotBlank(message = "Total area is required")
    Integer totalArea;

    @NotNull(message = "Payment policy is required")
    List<PaymentPolicyDto> paymentPolicies;

    List<BuildingDetailDto> buildingDetails;

    List<BuildingUnitDto> buildingUnits;

}