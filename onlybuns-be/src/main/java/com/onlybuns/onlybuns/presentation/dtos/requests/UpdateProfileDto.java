package com.onlybuns.onlybuns.presentation.dtos.requests;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDto {
    private String name;
    private String surname;
    private String password;
    private String confirmPassword;
    private AddressDto address;
}
