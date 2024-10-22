package com.onlybuns.onlybuns.presentation.dtos.requests;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdatePostDto {
    private Long id;
    private String description;
}
