package com.onlybuns.onlybuns.presentation.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto implements Serializable {
    private String data;
    private String mimeType;
    private LocalDateTime uploadedAt;
}
