package com.onlybuns.onlybuns.domain.services;

import com.onlybuns.onlybuns.domain.models.Image;
import com.onlybuns.onlybuns.infrastructure.interfaces.ImageRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class ImageCompressionService {
    @Value("${image.storage.directory}")
    private String imageStorageDirectory;

    private final ImageRepository imageRepository;

    public ImageCompressionService(ImageRepository imageRepository, ImageService imageService) {
        this.imageRepository = imageRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void compressOldImages() {
        // Find images older than a month and not yet compressed
        Iterable<Image> images = imageRepository
                .findByUploadedAtBeforeAndIsCompressedFalse(LocalDateTime.now().minusMonths(1));
        for (Image image : images) {
            try {
                // Construct the correct image path
                Path imagePath = Paths.get(imageStorageDirectory).resolve(image.getPath());
                
                // Print the image path for debugging

                // Compress image
                compressImage(imagePath);

                // Update the image record as compressed
                image.setCompressed(true);
                imageRepository.save(image);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void compressImage(Path imagePath) throws IOException {
        File imageFile = imagePath.toFile();

        // Get the mime type of the image
        String mimeType = Files.probeContentType(imagePath);
        String outputFormat = getFileExtensionFromMimeType(mimeType);

        // Create the "compressed" directory if it doesn't exist
        Path compressedDir = imagePath.getParent().resolve("compressed");
        if (!Files.exists(compressedDir)) {
            Files.createDirectories(compressedDir);
        }

        // Compress the image
        Thumbnails.of(imageFile)
                .scale(1) // Keep original scale
                .outputFormat(outputFormat)
                .outputQuality(0.7) // 70% quality
                .toFile(imagePath.getParent().resolve("compressed_" + imageFile.getName()).toFile());
    }

    // Helper method to map mime types to file extensions
    private String getFileExtensionFromMimeType(String mimeType) {
        switch (mimeType) {
            case "image/jpeg":
                return "jpg";
            case "image/png":
                return "png";
            case "image/gif":
                return "gif";
            default:
                return "jpg";
        }
    }
}