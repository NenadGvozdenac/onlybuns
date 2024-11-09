package com.onlybuns.onlybuns.presentation.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BareLocationDto {
    private String latitude;
    private String longitude;
}
