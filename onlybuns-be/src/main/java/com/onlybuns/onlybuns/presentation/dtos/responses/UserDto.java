package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.io.Serializable;

import com.onlybuns.onlybuns.presentation.dtos.requests.AddressDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto implements Serializable {
    private String username;
    private String name;
    private String surname;
    private String email;
    private AddressDto address;
}