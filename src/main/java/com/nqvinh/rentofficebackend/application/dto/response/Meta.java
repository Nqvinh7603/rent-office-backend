package com.nqvinh.rentofficebackend.application.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Meta {
    Integer page;
    Integer pageSize;
    Integer pages;
    Long total;
}