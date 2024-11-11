package com.onlybuns.onlybuns.presentation.controllers;

import com.onlybuns.onlybuns.domain.services.ImageCompressionService;
import com.onlybuns.onlybuns.domain.services.ImageService;
import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageController extends BaseController {

    @Value("${image.storage.directory}")
    private String imageStorageDirectory;

    private final ImageService imageService;
    private final ImageCompressionService imageCompressionService;

    @Autowired
    public ImageController(ImageService imageService,ImageCompressionService imageCompressionService) {
        this.imageService = imageService;
        this.imageCompressionService = imageCompressionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        var result = imageService.getImage(id);
        return createResponse(result);
    }

    @PostMapping("/upload")
    public ResponseEntity<Image> postImage(@RequestParam("file") MultipartFile file) {
        var result = imageService.saveImage(file);
        return createResponse(result);
    }
}
