package com.onlybuns.onlybuns.domain.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.domain.models.ChatUser;
import com.onlybuns.onlybuns.domain.models.User;
import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.ChatMessage;
import com.onlybuns.onlybuns.domain.models.ChatRoom;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatMessageRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatRoomRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatUserRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;
import com.onlybuns.onlybuns.presentation.dtos.requests.CreateChatRoomDto;
import com.onlybuns.onlybuns.presentation.dtos.responses.ChatMessageDto;
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
        chatUser.setJoinedAt(java.time.LocalDateTime.now().plusHours(1));
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

    public Result<List<ChatMessageDto>> getMessageForRoom(Long chatRoomId, String username) {
        var chatRoomOpt = chatRoomRepository.findById(chatRoomId);

        if (chatRoomOpt.isEmpty()) {
            return Result.failure("Chat room not found", 404);
        }

        // Pronađi korisnika koji je poslao zahtev
        List<ChatUser> chatUsers = chatUserRepository.findByUser(userRepository.findByUsername(username).get());

        if (chatUsers.isEmpty()) {
            return Result.failure("User not found", 404);
        }

        // pronadji usera iz te sobe
        ChatUser chatUser = chatUsers.stream()
                .filter(chatUser1 -> chatUser1.getChatRoom().getId().equals(chatRoomId))
                .findFirst()
                .orElse(null);

        ChatRoom chatRoom = chatRoomOpt.get();
        LocalDateTime userJoinedAt = chatUser.getJoinedAt();

        // Prvo, uzmi sve poruke u opadajućem redosledu
        List<ChatMessage> chatMessages = chatMessageRepository
                .findAllByChatRoomOrderByTimestampDesc(chatRoom);

        // Filtriraj poruke koje su poslate nakon što se korisnik pridružio
        List<ChatMessage> chatMessagesBeforeJoin = chatMessages.stream()
                .filter(chatMessage -> chatMessage.getTimestamp().isBefore(userJoinedAt)) // Poslate nakon što se
                                                                                          // korisnik pridružio
                .collect(Collectors.toList());

        // Zatim, uzmi samo poslednjih 10 poruka koje su poslate nakon `joinedAt`
        List<ChatMessage> last10MessagesBeforeJoin = chatMessagesBeforeJoin.stream()
                .limit(10) // Uzmi prvih 10 (jer su poruke već u opadajućem redosledu)
                .collect(Collectors.toList());

        List<ChatMessage> combinedMessages = new ArrayList<>();
        combinedMessages.addAll(chatMessages.stream()
                .filter(chatMessage -> chatMessage.getTimestamp().isAfter(userJoinedAt)) // Poslate pre nego što se
                                                                                         // korisnik pridružio
                .collect(Collectors.toList()));
        combinedMessages.addAll(last10MessagesBeforeJoin);

        // Definiši željeni format za datum i vreme
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, h:mm:ss a");

        // Mapiraj poruke u DTO objekat
        return Result.success(combinedMessages.stream().map(chatMessage -> {
            String formattedTimestamp = chatMessage.getTimestamp().format(formatter);
            return new ChatMessageDto(chatMessage.getSender().getUsername(), chatMessage.getText(), formattedTimestamp);
        }).toList());
    }

}
