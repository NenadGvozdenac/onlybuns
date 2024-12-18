package com.onlybuns.onlybuns.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.domain.models.ChatUser;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.ChatRoom;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatMessageRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatRoomRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatUserRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;
import com.onlybuns.onlybuns.presentation.dtos.requests.CreateChatRoomDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ChatRoomViewDto;

@Service
public class ChatRoomService {

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    // Metoda za dodavanje korisnika u grupu
    public Result<ChatRoomViewDto> addUserToChatRoom(Long chatRoomId, String username, String adminUsername) {

        var userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) {
            return Result.failure("User not found", 403);
        }

        User user = userOpt.get();

        var chatRoomOpt = chatRoomRepository.findById(chatRoomId);

        if (chatRoomOpt.isEmpty()) {
            return Result.failure("Chat room not found", 404);
        }

        ChatRoom chatRoom = chatRoomOpt.get();

        if (!chatRoom.getAdmin().getUsername().equals(adminUsername)) {
            return Result.failure("User is not admin of the group", 400);
        }

        if (chatRoom.getUsers().stream()
                .filter(chatUser -> chatUser.getUser().getUsername().equals(username))
                .findFirst()
                .isPresent()) {
            return Result.failure("User is already in the chat room", 401);
        }

        // Dodaj korisnika kao chatUser
        ChatUser chatUser = new ChatUser();
        chatUser.setChatRoom(chatRoom);
        chatUser.setUser(user);
        chatUser.setJoinedAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);

        // Dodaj chatUser u listu korisnika sobe
        chatRoom.getUsers().add(chatUser);
        chatRoomRepository.save(chatRoom);

        return Result
                .success(new ChatRoomViewDto(chatRoom.getId(), chatRoom.getName(), chatRoom.getAdmin().getUsername()));
    }

    public Result<CreateChatRoomDto> createChatRoom(String chatRoomName, String adminUsername) {

        var adminOpt = userRepository.findByUsername(adminUsername);
        if (adminOpt.isEmpty()) {
            return Result.failure("User not found", 404);
        }
        User admin = adminOpt.get();

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(chatRoomName);
        chatRoom.setAdmin(admin);
        ChatRoom chatRoomfinal = chatRoomRepository.save(chatRoom);

        // Kreiraj ChatUser entitet za admina
        ChatUser chatUser = new ChatUser();
        chatUser.setChatRoom(chatRoom);
        chatUser.setUser(admin);
        chatUser.setJoinedAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);

        chatRoomfinal.getUsers().add(chatUser);
        chatRoomRepository.save(chatRoom);

        CreateChatRoomDto chatRoomDto = new CreateChatRoomDto(chatRoom.getId(), chatRoom.getName());

        return Result.success(chatRoomDto);
    }

    public Result<List<ChatRoomViewDto>> getChatRoomsForUser(String username) {
        var userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return Result.failure("User not found", 404);
        }

        User user = userOpt.get();
        List<ChatUser> chatUsers = chatUserRepository.findByUser(user);

        return Result.success(chatUsers.stream().map(chatUser -> {
            ChatRoom chatRoom = chatUser.getChatRoom();
            return new ChatRoomViewDto(chatRoom.getId(), chatRoom.getName(), chatRoom.getAdmin().getUsername());
        }).toList());
    }

    public Result<ChatRoomViewDto> getChatRoomById(Long chatRoomId) {
        var chatRoomOpt = chatRoomRepository.findById(chatRoomId);

        if (chatRoomOpt.isEmpty()) {
            return Result.failure("Chat room not found", 404);
        }

        ChatRoom chatRoom = chatRoomOpt.get();
        return Result
                .success(new ChatRoomViewDto(chatRoom.getId(), chatRoom.getName(), chatRoom.getAdmin().getUsername()));
    }

}
