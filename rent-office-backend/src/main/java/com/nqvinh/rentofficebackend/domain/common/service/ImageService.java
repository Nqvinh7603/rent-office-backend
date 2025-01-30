package com.nqvinh.rentofficebackend.domain.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface ImageService {
    CompletableFuture<String> handleImageUpload(MultipartFile file, String url);
}
