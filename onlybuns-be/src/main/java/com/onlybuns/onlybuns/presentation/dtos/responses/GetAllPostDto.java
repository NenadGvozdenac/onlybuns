package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GetAllPostDto {
    private Long id;
    private ImageDto image;
    private List<CommentDto> comments = new ArrayList<>();
    private LocalDateTime dateOfCreation;
    private String description;
    private int numberOfLikes;
    private String username;
    private boolean isMarkedForAdvertisement;
    private List<UserDto> users;
    private AddressDto address;
}
