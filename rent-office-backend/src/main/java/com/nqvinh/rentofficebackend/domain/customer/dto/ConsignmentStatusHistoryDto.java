/*******************************************************************************
 * Class        ：ConsignmentStatusHistory
 * Created date ：2025/03/04
 * Lasted date  ：2025/03/04
 * Author       ：vinhNQ2
 * Change log   ：2025/03/04：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * ConsignmentStatusHistory
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
public class ConsignmentStatusHistoryDto {
    Long consignmentStatusHistoryId;

    @NotBlank(message = "Status is required")
    String status;

    String note;

    @PastOrPresent(message = "Created at must be in the past or present")
    LocalDateTime createdAt;

    @PastOrPresent(message = "Updated at must be in the past or present")
    LocalDateTime updatedAt;

    String createdBy;
    String updatedBy;
}