package com.nqvinh.rentofficebackend.application.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchCriteria {
    String key;
    String operation;
    Object value;
}

