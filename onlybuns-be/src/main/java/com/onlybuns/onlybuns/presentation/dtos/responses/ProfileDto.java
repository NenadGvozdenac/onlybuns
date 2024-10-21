package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {
    private String username;
    private String name;
    private String surname;
    private String email;
    private List<PostDto> activePosts;
}
