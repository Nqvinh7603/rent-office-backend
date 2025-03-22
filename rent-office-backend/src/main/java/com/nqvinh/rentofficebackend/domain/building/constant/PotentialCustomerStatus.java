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
    CONTACTED_NO_RESPONSE, //  đã liên hệ _ chưa phản hồi
    CONTACTED_SCHEDULED, // đã liên hệ _ HẸN GỌI LẠI
    NOT_CONTACTED,// chưa liên hệ
    DEAL_DONE, // đã chấp nhận
    IN_PROGRESS, // đang xử lý
    CANCELED // không còn nhu cầu
}