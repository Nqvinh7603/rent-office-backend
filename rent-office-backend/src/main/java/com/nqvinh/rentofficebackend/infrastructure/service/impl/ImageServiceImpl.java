package com.nqvinh.rentofficebackend.infrastructure.service.impl;

import com.nqvinh.rentofficebackend.infrastructure.service.ImageService;
import com.nqvinh.rentofficebackend.infrastructure.utils.CloudinaryUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ImageServiceImpl implements ImageService {

    CloudinaryUtils cloudinaryUtils;

    @Override
    @SneakyThrows
    public String handleImageUpload(MultipartFile file, String url) {
        if (file != null && !file.isEmpty()) {
            if (url != null && !url.isEmpty()) {
                String publicId = cloudinaryUtils.getPublicIdFromCloudinary(url);
                cloudinaryUtils.deleteFileFromCloudinary(publicId);
            }
            File convFile = cloudinaryUtils.convertMultipartFileToFile(file);
            return cloudinaryUtils.uploadFileToCloudinary(convFile);
        }
        return url;
    }
}
