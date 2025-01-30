package com.nqvinh.rentofficebackend.infrastructure.service;

import com.nqvinh.rentofficebackend.domain.common.service.ImageService;
import com.nqvinh.rentofficebackend.infrastructure.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ImageServiceImpl implements ImageService {

    CloudinaryUtils cloudinaryUtils;

    @Override
    @Async
    @SneakyThrows
    public CompletableFuture<String> handleImageUpload(MultipartFile file, String url) {
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
    }
}
