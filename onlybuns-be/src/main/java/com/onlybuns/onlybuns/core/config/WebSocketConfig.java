package com.onlybuns.onlybuns.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlybuns.onlybuns.domain.models.ChatMessage;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatMessageRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatRoomRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.ChatUserRepository;
import com.onlybuns.onlybuns.infrastructure.interfaces.UserRepository;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatUserRepository chatUserRepository;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserRepository userRepository;

    private final Map<String, List<WebSocketSession>> rooms = new HashMap<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("Registering WebSocket handler for /ws");
        registry.addHandler(webSocketHandler(), "/ws").setAllowedOrigins("http://localhost:5173");
    }

    @Bean
    public WebSocketHandler webSocketHandler() {
        return new TextWebSocketHandler() {

            @Override
            public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                String payload = message.getPayload();
                ObjectMapper objectMapper = new ObjectMapper(); // Use Jackson or any JSON library
                Map<String, String> data = objectMapper.readValue(payload, Map.class);

                if ("join_room".equals(data.get("type"))) {
                    String roomId = data.get("roomId");
                    String username = data.get("username");

                    joinRoom(session, roomId);

                    // Notify about the user joining
                    String joinMessage = createJsonMessage("info", "User " + username + " has joined the chat!");
                    sendMessageToRoom(session, joinMessage, roomId);

                    System.out.println("User " + username + " joined room: " + roomId);
                } else if ("new_message".equals(data.get("type"))) {

                    System.out.println("New message received: " + data);
                    // Handle other types of messages
                    String roomId = (String) data.get("roomId");
                    String username = (String) data.get("username");
                    String text = (String) data.get("text");

                    System.out.println(roomId + username + text);

                    ChatMessage chatMessage = new ChatMessage();
                    long roomIdLong = Long.parseLong(roomId);
                    chatMessage.setChatRoom(chatRoomRepository.findById(roomIdLong));
                    chatMessage.setSender(userRepository.findByUsername(username).get()); 
                    chatMessage.setText(text);
                    chatMessage.setTimestamp(java.time.LocalDateTime.now().plusHours(1));


                    System.out.println(chatMessage.getId() + chatMessage.getChatRoom().getName() + chatMessage.getSender().getUsername() + chatMessage.getText() + chatMessage.getTimestamp());

                    chatMessageRepository.save(chatMessage);

                    String messageJson = createJsonMessage("chat", chatMessage);
                    sendMessageToRoom(session, messageJson, roomId);
                }
            } 

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                System.out.println("Connection established: " + session.getId());
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
                rooms.values().forEach(room -> room.remove(session)); 
                System.out.println("Connection closed: " + session.getId());
            }

            private void joinRoom(WebSocketSession session, String roomId) {
                rooms.computeIfAbsent(roomId, k -> new ArrayList<>());
                List<WebSocketSession> roomSessions = rooms.get(roomId);

                if (!roomSessions.contains(session)) {
                    roomSessions.add(session);
                }
            }

            private void sendMessageToRoom(WebSocketSession sender, String message, String roomId) throws Exception {
                List<WebSocketSession> roomSessions = rooms.get(roomId);

                if (roomSessions != null) {
                    for (WebSocketSession session : roomSessions) {
                        if (session.isOpen()) { // Proveri da li je sesija još uvek otvorena
                            session.sendMessage(new TextMessage(message));
                        }
                    }
                }
            }

            private String createJsonMessage(String type, String message) throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> response = new HashMap<>();
                response.put("type", type);
                response.put("message", message);
                return objectMapper.writeValueAsString(response);
            }

            private String createJsonMessage(String type, ChatMessage chatMessage) throws Exception {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> response = new HashMap<>();
                response.put("type", type);
                response.put("text", chatMessage.getText());
                response.put("sender", chatMessage.getSender().getUsername());
                response.put("timestamp", chatMessage.getTimestamp().toString());
                return objectMapper.writeValueAsString(response);
            }

        };
    }
}
