
package com.onlybuns.onlybuns.presentation.dtos.responses;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto implements Serializable {
    private String username;
    private String message;
    private String timestamp;
}

