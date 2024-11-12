package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalInformationDto implements Serializable {
    private String name;
    private String description;
    private BareLocationDto location;
}
