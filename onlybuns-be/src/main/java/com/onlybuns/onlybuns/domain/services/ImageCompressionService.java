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
                // Perform compression
                Path imagePath = Paths.get(imageStorageDirectory, image.getPath());

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
    
        // Assuming you already have mimeType from image metadata or another source
        String mimeType = Files.probeContentType(imagePath);  // Get mime type based on the file's content
    
        // Determine the format based on mime type
        String outputFormat = getFileExtensionFromMimeType(mimeType);
    
        // Create the "compressed" directory if it doesn't exist
        Path compressedDir = imagePath.getParent().resolve("compressed");
        if (!Files.exists(compressedDir)) {
            Files.createDirectories(compressedDir); // Create the directory if it doesn't exist
        }
    
        // Compress the image with the appropriate output format
        Thumbnails.of(imageFile)
                  .outputFormat(outputFormat)  // Use the appropriate format based on mime type
                  .outputQuality(0.7)  // 70% quality
                  .toFile(compressedDir.resolve("compressed_" + imageFile.getName()).toFile());
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