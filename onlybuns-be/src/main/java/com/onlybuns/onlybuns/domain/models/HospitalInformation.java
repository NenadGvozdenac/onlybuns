package com.onlybuns.onlybuns.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HospitalInformation {

    @JsonProperty("_id")
    private String id;
    private String name;
    private String description;
    private AddressInformation location;

    @JsonProperty("__v")
    private int version;

    @Override
    public String toString() {
        return "HospitalInformation{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", version=" + version +
                '}';
    }
}
