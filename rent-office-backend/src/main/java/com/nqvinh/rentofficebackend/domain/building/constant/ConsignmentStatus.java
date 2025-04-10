package com.nqvinh.rentofficebackend.domain.building.constant;

public enum ConsignmentStatus {
    PENDING, // Chờ xác nhận
    CONFIRMED, // Đã xác nhận
    CANCELLED, // Đã từ chối
    INCOMPLETE, // Thiếu thông tin
    ADDITIONAL_INFO, // Bổ sung thông tin
}
