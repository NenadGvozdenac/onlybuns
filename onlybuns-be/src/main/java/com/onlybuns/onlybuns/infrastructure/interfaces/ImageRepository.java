package com.onlybuns.onlybuns.infrastructure.interfaces;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import com.onlybuns.onlybuns.domain.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Iterable<Image> findByUploadedAtBeforeAndIsCompressedFalse(LocalDateTime uploadedAt);
}
