package com.onlybuns.onlybuns.domain.serviceinterfaces;

import java.util.List;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.presentation.dtos.requests.CreateChatRoomDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ChatMessageDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ChatRoomViewDto;

public interface ChatRoomInterface {
    public Result<ChatRoomViewDto> addUserToChatRoom(Long chatRoomId, String username, String adminUsername);
    public Result<ChatRoomViewDto> removeUserFromChatRoom(Long chatRoomId, String username, String adminUsername);
    public Result<CreateChatRoomDto> createChatRoom(String chatRoomName, String adminUsername);
    public Result<List<ChatRoomViewDto>> getChatRoomsForUser(String username);
    public Result<ChatRoomViewDto> getChatRoomById(Long chatRoomId, String username);
    public Result<List<ChatMessageDto>> getMessageForRoom(Long chatRoomId, String username);    
}
