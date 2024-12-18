package com.onlybuns.onlybuns.presentation.dtos.requests;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateChatRoomDto implements Serializable {
    private Long id;
    private String name;
}
