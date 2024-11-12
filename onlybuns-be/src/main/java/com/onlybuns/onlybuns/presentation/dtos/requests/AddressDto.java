package com.onlybuns.onlybuns.presentation.dtos.requests;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto implements Serializable {
    private String street;
    private String number;
    private String city;
    private String country;
    private Double longitude;
    private Double latitude;
}
