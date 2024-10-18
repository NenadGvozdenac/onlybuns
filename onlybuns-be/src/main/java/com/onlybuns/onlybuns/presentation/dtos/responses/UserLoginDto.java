package com.onlybuns.onlybuns.presentation.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserLoginDto {
    private String username;
    private String accessToken;
}
