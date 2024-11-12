package com.onlybuns.onlybuns.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressInformation {

    @JsonProperty("_id")
    private String id;
    
    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return "AddressInformation{" +
                "id='" + id + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}