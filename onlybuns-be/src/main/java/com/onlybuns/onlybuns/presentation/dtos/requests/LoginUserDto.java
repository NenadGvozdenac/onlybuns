package com.onlybuns.onlybuns.presentation.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginUserDto {
    private String username;
    private String password;
}
