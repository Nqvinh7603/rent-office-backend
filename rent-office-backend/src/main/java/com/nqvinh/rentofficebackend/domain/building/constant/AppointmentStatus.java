/*******************************************************************************
 * Class        ：AppointmentStatus
 * Created date ：2025/03/21
 * Lasted date  ：2025/03/21
 * Author       ：vinhNQ2
 * Change log   ：2025/03/21：01-00 vinhNQ2 create a new
 ******************************************************************************/
package com.nqvinh.rentofficebackend.domain.building.constant;

/**
 * AppointmentStatus
 *
 * @author vinhNQ2
 * @version 01-00
 * @since 01-00
 */
public enum AppointmentStatus {
    PENDING,          // Cuộc hẹn vừa được tạo, đang chờ xử lý
    CONFIRMED,        // Cuộc hẹn đã được xác nhận
    IN_PROGRESS,      // Cuộc hẹn đang diễn ra (khách hàng đang xem văn phòng)
    COMPLETED,        // Cuộc hẹn đã hoàn tất (khách hàng đã xem xong)
    SUCCESSFUL,       // Cuộc hẹn thành công (khách hàng đồng ý thuê văn phòng)
    UNSUCCESSFUL,     // Cuộc hẹn không thành công (khách hàng không đồng ý thuê)
    CANCELLED,        // Cuộc hẹn bị hủy
    FOLLOW_UP,        // Cần theo dõi thêm (ví dụ: khách hàng chưa quyết định, cần liên hệ lại)
    RESCHEDULED       // Cuộc hẹn được lên lịch lại (khách hàng yêu cầu đổi thời gian)
}