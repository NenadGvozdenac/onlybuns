package com.onlybuns.onlybuns.presentation.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private String username;
    private String name;
    private String surname;
    private String email;
}