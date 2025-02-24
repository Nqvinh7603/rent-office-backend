package com.nqvinh.rentofficebackend.domain.customer.constant;

public enum ConsignmentStatus {
    PENDING, // Chờ xác nhận
    CONFIRMED, // Đã xác nhận
    CANCELLED, // Đã từ chối
    INCOMPLETE, // Thiếu thông tin
}
