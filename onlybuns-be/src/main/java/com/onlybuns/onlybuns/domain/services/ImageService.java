package com.onlybuns.onlybuns.domain.services;

import com.onlybuns.onlybuns.domain.models.Image;
import com.onlybuns.onlybuns.infrastructure.interfaces.ImageRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${image.storage.directory}")
    private String imageStorageDirectory;

    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public byte[] getImage(long id) {
        Image image = imageRepository.getById(id);
        Path imagePath = Paths.get(image.getPath());
        try {
            if (!Files.exists(imagePath)) {
                throw new IOException("Image not found: " + imagePath.getFileName().toString());
            }
            return Files.readAllBytes(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getImageBase64(long id) {
        byte[] imageBytes = getImage(id);
        if (imageBytes != null) {
            return Base64.getEncoder().encodeToString(imageBytes);
        }
        return null;
    }

    public Image saveImage(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }

        String imageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path imagePath = Paths.get(imageStorageDirectory, imageName);

        try {
            Files.copy(file.getInputStream(), imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create image metadata
        Image image = new Image();
        image.setPath(imagePath.toString());
        image.setUploadedAt(LocalDateTime.now());
        image.setCompressed(false);
        image.setMimetype(imageName.substring(imageName.lastIndexOf(".") + 1));

        // Save image record to the database
        return imageRepository.save(image);
    }
}
