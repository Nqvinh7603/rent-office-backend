package com.nqvinh.rentofficebackend.application.enums;

public enum MessageEnums {
    CREATED_SUCCESS("created successfully"),
    FETCHED_SUCCESS("fetched successfully"),
    UPDATED_SUCCESS("updated successfully"),
    DELETED_SUCCESS("deleted successfully"),
    CREATION_ERROR("Error creating"),
    FETCH_ERROR("Error fetching"),
    UPDATE_ERROR("Error updating"),
    DELETION_ERROR("Error deleting");

    private final String message;

    MessageEnums(String message) {
        this.message = message;
    }

    public String getMessage(String moduleName) {
        return moduleName + " " + message;
    }

    public String getError(String moduleName) {
        return message + " in " + moduleName;
    }
}

