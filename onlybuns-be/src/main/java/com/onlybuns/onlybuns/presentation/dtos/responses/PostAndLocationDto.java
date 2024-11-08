package com.onlybuns.onlybuns.presentation.dtos.responses;

import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PostAndLocationDto {
    private Long id;
    private String description;
    private ImageDto image;
    private String username;
    private AddressDto address;
}
