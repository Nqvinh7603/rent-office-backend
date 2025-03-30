/*******************************************************************************
 * Class        ：AppointmentBuildingStatus
 * Created date ：2025/03/26
 * Lasted date  ：2025/03/26
 * Author       ：vinhNQ2
 * Change log   ：2025/03/26：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.constant;

/**
 * AppointmentBuildingStatus
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public enum AppointmentBuildingStatus {
    PENDING, // Mới tạo
    CONFIRMED, // Đã xác nhận
    VIEWED, // Đã xem
    SUCCESSFUL, // Khách đã thuê
    UNSUCCESSFUL, // Khách không thuê
    CANCELLED // Cuộc hẹn bị hủy hoặc không diễn ra
}