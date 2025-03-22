/*******************************************************************************
 * Class        ：BuildingUnitStatus
 * Created date ：2025/03/17
 * Lasted date  ：2025/03/17
 * Author       ：vinhNQ2
 * Change log   ：2025/03/17：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.constant;

/**
 * BuildingUnitStatus
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public enum BuildingUnitStatus {
    AVAILABLE,          // Đang có sẵn để thuê
    RENTED,             // Đã được thuê
    UNDER_MAINTENANCE,  // Đang bảo trì
    RESERVED,           // Đã có người đặt trước
    UNAVAILABLE         // Không thể cho thuê (chủ tòa nhà không muốn cho thuê)
}