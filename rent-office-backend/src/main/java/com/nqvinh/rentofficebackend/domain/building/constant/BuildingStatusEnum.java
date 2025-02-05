package com.nqvinh.rentofficebackend.domain.building.constant;

public enum BuildingStatusEnum {
    AVAILABLE, // Có sẵn
    OCCUPIED, // Đã thuê
    UNDER_MAINTENANCE, // Đang bảo trì
    RESERVED, // Đã đặt trước
    UNAVAILABLE; // Không có sẵn
}
