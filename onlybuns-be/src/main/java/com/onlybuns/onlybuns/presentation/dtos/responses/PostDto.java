package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private Long id;
    private LocalDateTime dateOfCreation;
    private String description;
    private int numberOfLikes;
}