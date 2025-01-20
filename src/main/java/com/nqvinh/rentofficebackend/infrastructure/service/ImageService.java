package com.nqvinh.rentofficebackend.infrastructure.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String handleImageUpload(MultipartFile file, String url);
}
