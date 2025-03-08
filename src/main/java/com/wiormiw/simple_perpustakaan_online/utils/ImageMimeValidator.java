package com.wiormiw.simple_perpustakaan_online.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageMimeValidator {
    private static final List<String> ALLOWED_TYPES = List.of("image/png", "image/jpeg", "image/jpg", "image/webp");

    private ImageMimeValidator() {
        // Private constructor to prevent instantiation
    }

    public static boolean isNotValidImage(MultipartFile file) {
        return file == null || !ALLOWED_TYPES.contains(file.getContentType());
    }
}
