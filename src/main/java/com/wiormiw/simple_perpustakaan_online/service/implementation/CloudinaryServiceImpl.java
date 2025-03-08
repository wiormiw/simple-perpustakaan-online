package com.wiormiw.simple_perpustakaan_online.service.implementation;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.wiormiw.simple_perpustakaan_online.config.exception.ImageProcessingException;
import com.wiormiw.simple_perpustakaan_online.service.CloudinaryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {
    @Resource
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folderName) throws ImageProcessingException {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("upload-", "-" + file.getOriginalFilename());
            Files.write(tempFile, file.getBytes());

            var uploadedFile = cloudinary.uploader().upload(tempFile.toFile(), ObjectUtils.asMap("folder", folderName));
            return uploadedFile.get("url").toString();
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to upload image: " + e.getMessage());
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println("Warning: Failed to delete temporary file: " + tempFile);
                }
            }
        }
    }
}
