package com.nqvinh.rentofficebackend.infrastructure.service;

import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import com.nqvinh.rentofficebackend.infrastructure.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
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
        try {
            if (file != null && !file.isEmpty()) {
                if (url != null && !url.isEmpty()) {
                    String publicId = cloudinaryUtils.getPublicIdFromCloudinary(url);
                    cloudinaryUtils.deleteFileFromCloudinary(publicId);
                }
                File convFile = cloudinaryUtils.convertMultipartFileToFile(file);
                String uploadedUrl = cloudinaryUtils.uploadFileToCloudinary(convFile);

                if (convFile != null && convFile.exists()) {
                    convFile.delete();
                }

                return CompletableFuture.completedFuture(uploadedUrl);
            }
            return CompletableFuture.completedFuture(url);
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Override
    @Async
    public CompletableFuture<List<String>> handleImageUpload(List<MultipartFile> files, List<String> urls) {
        List<String> urlsCopy = new ArrayList<>(urls);
        List<CompletableFuture<String>> futures = files.stream()
                .map(file -> {
                    String url = urlsCopy.isEmpty() ? null : urlsCopy.remove(0);
                    return handleImageUpload(file, url);
                })
                .collect(Collectors.toList());

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        return allOf.thenApply(v -> futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList()));
    }
}