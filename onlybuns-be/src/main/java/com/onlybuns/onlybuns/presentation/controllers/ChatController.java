package com.onlybuns.onlybuns.presentation.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.onlybuns.onlybuns.domain.services.ChatRoomService;
import com.onlybuns.onlybuns.presentation.dtos.requests.AddUserToChatRoomDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.CreateChatRoomDto;
import com.onlybuns.onlybuns.presentation.dtos.requests.CreateRoomDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ChatMessageDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ChatRoomViewDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Controller
@RequestMapping("/chat")
public class ChatController extends BaseController {

    @Autowired
    private ChatRoomService chatRoomService; 

    @Operation(summary = "Create a room for chating", description = "This endpoint allows a user to create a room for chating")
    @ApiResponse(responseCode = "403", description = "User not found.")
    @ApiResponse(responseCode = "400", description = "User is not admin of the group")
    @ApiResponse(responseCode = "404", description = "Chat room not found.")
    @PostMapping("/create")
    public ResponseEntity<CreateChatRoomDto> createChatRoom(@RequestBody CreateRoomDto dto) {
        var result = chatRoomService.createChatRoom(dto.getName(), getLoggedInUsername());
        return createResponse(result);
    }

    @Operation(summary = "Add user into a room for chating", description = "This endpoint allows a user to create a room for chating")
    @ApiResponse(responseCode = "403", description = "User not found.")
    @ApiResponse(responseCode = "400", description = "User is not admin of the group")
    @ApiResponse(responseCode = "401", description = "User is already in the chat room")
    @ApiResponse(responseCode = "404", description = "Chat room not found.")
    @PostMapping("/addUser")
    public ResponseEntity<ChatRoomViewDto> addUserToChatRoom(@RequestBody AddUserToChatRoomDto addDto) {
       var result = chatRoomService.addUserToChatRoom(addDto.getId(), addDto.getUsername(), getLoggedInUsername()); 
       return createResponse(result);     
    }

    @Operation(summary = "Get chat rooms", description = "This endpoint allows a user to get chat rooms")
    @ApiResponse(responseCode = "404", description = "User not found.")
    @GetMapping("/myRooms")
    public ResponseEntity<List<ChatRoomViewDto>> getChatRooms() {
        var result = chatRoomService.getChatRoomsForUser(getLoggedInUsername());
        return createResponse(result);
    }

    @Operation(summary = "Get chat room", description = "This endpoint allows a user to get chat room")
    @GetMapping("/room")
    public ResponseEntity<ChatRoomViewDto> getChatRoom(@RequestParam Long id) {
        var result = chatRoomService.getChatRoomById(id);
        return createResponse(result);
    }

    @Operation(summary = "Get chat room messages", description = "This endpoint allows a user to get chat room messages")
    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessageDto>> getChatRoomMessages(@RequestParam Long id) {
        var result = chatRoomService.getMessageForRoom(id, getLoggedInUsername());
        return createResponse(result);
    }
}
