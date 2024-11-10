package com.onlybuns.onlybuns.presentation.controllers;

import com.onlybuns.onlybuns.domain.services.ImageService;
import com.onlybuns.onlybuns.infrastructure.interfaces.ImageRepository;

import jakarta.transaction.Transactional;

import com.onlybuns.onlybuns.domain.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Value("${image.storage.directory}")
    private String imageStorageDirectory;

    private final ImageService imageService;
    private final ImageRepository imageRepository;

    @Autowired
    public ImageController(ImageService imageService, ImageRepository imageRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        try {
            byte[] imageBytes = imageService.getImage(id);
            String mimeType = imageRepository.getById(id).getMimetype();
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> postImage(@RequestParam("file") MultipartFile file) {
        Image image = imageService.saveImage(file);
        return new ResponseEntity<>("Image uploaded successfully: " + image.getPath(), HttpStatus.CREATED);
    }
}
