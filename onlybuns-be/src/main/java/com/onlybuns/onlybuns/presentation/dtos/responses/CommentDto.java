package com.onlybuns.onlybuns.presentation.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommentDto {
    
    private Long id;
    private String comment;
    private LocalDateTime commentedAt;
    private String username;
}
