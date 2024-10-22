package com.onlybuns.onlybuns.presentation.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private String data;
    private String mimeType;
    private LocalDateTime uploadedAt;
}
