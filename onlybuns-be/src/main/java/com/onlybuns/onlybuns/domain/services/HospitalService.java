package com.onlybuns.onlybuns.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.HospitalInformation;
import com.onlybuns.onlybuns.domain.serviceinterfaces.HospitalServiceInterface;

@Service
public class HospitalService implements HospitalServiceInterface {

    private WebSocketSession webSocketSession;
    private final String nodeServerUrl = "ws://vets-and-pets-be:3000";
    private List<HospitalInformation> hospitalInformationList = new ArrayList<>();
    private CompletableFuture<Result<List<HospitalInformation>>> futureResult = new CompletableFuture<>();

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Result<List<HospitalInformation>> connectToNodeServer() {
        // Clear the hospital information list every time the connection is attempted
        clearQueue();
        
        if (webSocketSession != null && webSocketSession.isOpen()) {
            return Result.failure(null, 500);  // Connection is already open
        }

        try {
            StandardWebSocketClient client = new StandardWebSocketClient();
            WebSocketConnectionManager manager = new WebSocketConnectionManager(client, new HospitalHandler(), nodeServerUrl);
            manager.start();
            
            // Block and wait for the future to complete
            return futureResult.get();  // This will return the result once the WebSocket connection is closed and processed
        } catch (Exception e) {
            return Result.failure("Failed to establish connection: " + e.getMessage(), 500);
        }
    }

    private void clearQueue() {
        hospitalInformationList.clear();
        futureResult = new CompletableFuture<>();
    }

    private class HospitalHandler extends TextWebSocketHandler {
        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) {
            try {
                HospitalInformation hospitalInformation = objectMapper.readValue(message.getPayload(), HospitalInformation.class);

                if (hospitalInformation != null) {
                    hospitalInformationList.add(hospitalInformation);
                    System.out.println("Received hospital information: " + hospitalInformation);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            // Once the connection is closed, complete the future with the result
            futureResult.complete(Result.success(hospitalInformationList));
        }
    }
}
