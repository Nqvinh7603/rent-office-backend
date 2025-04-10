/*******************************************************************************
 * Class        ：CustomerStatus
 * Created date ：2025/03/07
 * Lasted date  ：2025/03/07
 * Author       ：vinhNQ2
 * Change log   ：2025/03/07：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.constant;

/**
 * CustomerStatus
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public enum PotentialCustomerStatus {
    NOT_CONTACTED,         // Chưa liên hệ
    CONTACTED,             // Đã liên hệ (gộp cả "đã liên hệ chưa phản hồi", "hẹn gọi lại")
    DEAL_IN_PROGRESS,      // Đang xử lý deal (đã vào phễu)
    DEAL_DONE,             // Đã chốt thuê
    CANCELED               // Không còn nhu cầu
}

