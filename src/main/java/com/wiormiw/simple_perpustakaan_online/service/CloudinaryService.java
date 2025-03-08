package com.wiormiw.simple_perpustakaan_online.service;

import com.wiormiw.simple_perpustakaan_online.config.exception.ImageProcessingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CloudinaryService {
    public String uploadImage(MultipartFile file, String folderName) throws ImageProcessingException;
}
