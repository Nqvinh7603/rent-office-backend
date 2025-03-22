/*******************************************************************************
 * Class        ：ContractStatus
 * Created date ：2025/03/17
 * Lasted date  ：2025/03/17
 * Author       ：vinhNQ2
 * Change log   ：2025/03/17：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.contract.constant;

/**
 * ContractStatus
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public enum ContractStatus {
    PENDING, // chờ xử lý
    SIGNED, // đã ký
    ACTIVE, // có hiệu lực
    EXPIRED, // hết hạn
    CANCELLED // đã huỷ
}