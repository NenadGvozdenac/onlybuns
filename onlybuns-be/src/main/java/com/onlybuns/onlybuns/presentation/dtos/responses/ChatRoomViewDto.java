package com.onlybuns.onlybuns.presentation.dtos.responses;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomViewDto implements Serializable {
    private Long id;
    private String name;
    private String admin;
}

