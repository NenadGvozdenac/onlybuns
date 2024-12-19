package com.onlybuns.onlybuns.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

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
                // Assuming the payload format is "join_room:roomId"
                if (payload.startsWith("join_room:")) {
                    String roomId = payload.split(":")[1];
                    joinRoom(session, roomId);
                    session.sendMessage(new TextMessage("You have joined room: " + roomId));
                    System.out.println("User " + session.getId() + " joined room: " + roomId);
                } else {
                    // Handle other types of messages (chat, etc.)
                    sendMessageToRoom(session, payload);
                }
            }

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                System.out.println("Connection established: " + session.getId());
            }

            @Override
            public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
                // Handle cleanup when a connection closes
                rooms.values().forEach(room -> room.remove(session));
                System.out.println("Connection closed: " + session.getId());
            }

            private void joinRoom(WebSocketSession session, String roomId) {
                // Add session to the room
                rooms.computeIfAbsent(roomId, k -> new ArrayList<>()).add(session);
            }

            private void sendMessageToRoom(WebSocketSession sender, String message) throws Exception {
                // Broadcast the message to all clients in the same room
                for (Map.Entry<String, List<WebSocketSession>> entry : rooms.entrySet()) {
                    for (WebSocketSession session : entry.getValue()) {
                        if (session != sender) {
                            session.sendMessage(new TextMessage(message));
                        }
                    }
                }
            }
        };
    }
}
