package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto {
    private Long id;
    private ImageDto image;
    private LocalDateTime dateOfCreation;
    private String description;
    private int numberOfLikes;
    private List<CommentDto> comments;
    private List<UserDto> users;
}