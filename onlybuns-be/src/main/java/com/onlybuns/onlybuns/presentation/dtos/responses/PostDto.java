package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostDto implements Serializable {
    private Long id;
    private ImageDto image;
    private LocalDateTime dateOfCreation;
    private String description;
    private int numberOfLikes;
    private String username;
    private AddressDto address;
    private List<CommentDto> comments;
    private List<UserDto> users;
}