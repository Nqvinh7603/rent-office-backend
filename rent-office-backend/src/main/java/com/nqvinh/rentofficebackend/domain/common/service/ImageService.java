package com.nqvinh.rentofficebackend.domain.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ImageService {
    CompletableFuture<List<String>> handleImageUpload(List<MultipartFile> files, List<String> urls);
    CompletableFuture<String> handleImageUpload(MultipartFile file, String url);
}
