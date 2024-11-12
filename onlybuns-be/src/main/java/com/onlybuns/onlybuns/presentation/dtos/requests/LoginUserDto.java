package com.onlybuns.onlybuns.presentation.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginUserDto {
    private String email;
    private String password;
}
