package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.util.List;

import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;

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
    private AddressDto address;
    private List<PostDto> activePosts;
    private List<UserDto> followers;
    private List<UserDto> following;
}
