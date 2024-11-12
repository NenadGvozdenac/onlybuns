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
public class RegisterUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String name;
    private String surname;
    private AddressDto address;
}