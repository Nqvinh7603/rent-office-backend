package com.nqvinh.rentofficebackend.infrastructure.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CloudinaryUtils {

    Cloudinary cloudinary;

    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

//    public String uploadFileToCloudinary(File file) throws IOException {
//        var uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "/ct553/"));
//        return uploadResult.get("url").toString();
//    }


    public String uploadFileToCloudinary(MultipartFile file) throws IOException {
        var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "/ct553/"));
        return uploadResult.get("url").toString();
    }


    public void deleteFileFromCloudinary(String name) throws IOException {
        cloudinary.uploader().destroy(name, ObjectUtils.emptyMap());
    }

    public String getPublicIdFromCloudinary(String imageUrl) {
        String[] parts = imageUrl.split("/");
        return parts[parts.length - 2] + "/" + parts[parts.length - 1].substring(0, parts[parts.length - 1].lastIndexOf('.'));
    }

}

