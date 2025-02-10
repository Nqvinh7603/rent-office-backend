package com.nqvinh.rentofficebackend.infrastructure.service;

import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import com.nqvinh.rentofficebackend.infrastructure.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ImageServiceImpl implements ImageService {

    CloudinaryUtils cloudinaryUtils;

    @Override
    @Async
    public CompletableFuture<String> handleImageUpload(MultipartFile file, String url) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (file != null && !file.isEmpty()) {
                    if (url != null && !url.isEmpty()) {
                        cloudinaryUtils.deleteFileFromCloudinary(cloudinaryUtils.getPublicIdFromCloudinary(url));
                    }
                    return cloudinaryUtils.uploadFileToCloudinary(file);
                }
                return url;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    @Async
    public CompletableFuture<List<String>> handleImageUpload(List<MultipartFile> files, List<String> urls) {
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> {
                    String existingUrl = urls.isEmpty() ? null : urls.remove(0);
                    return (existingUrl != null && !existingUrl.isEmpty()) ?
                            CompletableFuture.completedFuture(existingUrl) :
                            handleImageUpload(file, existingUrl);
                })
                .collect(Collectors.toList());

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }
}