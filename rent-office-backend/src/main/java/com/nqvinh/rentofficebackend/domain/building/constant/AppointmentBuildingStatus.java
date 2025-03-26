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
    PENDING,      // Tòa nhà vừa được thêm vào cuộc hẹn, đang chờ xử lý
    CONFIRMED,    // Tòa nhà đã được xác nhận sẽ xem trong cuộc hẹn
    VIEWED,       // Tòa nhà đã được khách hàng xem
    SUCCESSFUL,   // Khách hàng đồng ý thuê văn phòng trong tòa nhà
    UNSUCCESSFUL, // Khách hàng không đồng ý thuê văn phòng trong tòa nhà
    CANCELLED,    // Tòa nhà bị hủy (khách hàng không muốn xem nữa)
    RESCHEDULED   // Thời gian xem tòa nhà được lên lịch lại
}