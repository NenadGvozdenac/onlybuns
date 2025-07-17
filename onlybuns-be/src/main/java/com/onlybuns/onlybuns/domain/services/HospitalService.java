package com.onlybuns.onlybuns.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

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
    private final String messageQueueUrl = "ws://message-queue:4000/ws?queue=hospital-data";
    private final String vetsAndPetsUrl = "http://vets-and-pets-be:3000";
    private List<HospitalInformation> hospitalInformationList = new ArrayList<>();
    private CompletableFuture<Result<List<HospitalInformation>>> futureResult = new CompletableFuture<>();
    private volatile boolean dataReceivingComplete = false;

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
            // First, trigger Vets&Pets to send data to message queue
            triggerVetsToSendData();
            
            // Then connect to message queue to receive the data
            StandardWebSocketClient client = new StandardWebSocketClient();
            WebSocketConnectionManager manager = new WebSocketConnectionManager(client, new HospitalHandler(), messageQueueUrl);
            manager.start();
            
            // Block and wait for the future to complete with timeout
            return futureResult.get(30, TimeUnit.SECONDS);  // 30 second timeout
        } catch (Exception e) {
            if (webSocketSession != null && webSocketSession.isOpen()) {
                try {
                    webSocketSession.close();
                } catch (Exception closeException) {
                    System.err.println("Error closing WebSocket: " + closeException.getMessage());
                }
            }
            return Result.failure("Failed to establish connection: " + e.getMessage(), 500);
        }
    }
    
    private void triggerVetsToSendData() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(vetsAndPetsUrl + "/api/send-vets-to-queue"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .header("Content-Type", "application/json")
                    .build();
                    
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                System.out.println("Successfully triggered Vets&Pets to send data to message queue");
            } else {
                System.err.println("Failed to trigger Vets&Pets: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error triggering Vets&Pets to send data: " + e.getMessage());
            // Don't fail the whole process, just log the error
        }
    }

    private void clearQueue() {
        hospitalInformationList.clear();
        futureResult = new CompletableFuture<>();
        dataReceivingComplete = false;
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close();
            } catch (Exception e) {
                System.err.println("Error closing previous WebSocket session: " + e.getMessage());
            }
        }
        webSocketSession = null;
    }

    private class HospitalHandler extends TextWebSocketHandler {
        private long lastMessageTime = System.currentTimeMillis();
        private final long MESSAGE_TIMEOUT = 5000; // 5 seconds timeout between messages
        
        @Override
        public void handleTextMessage(WebSocketSession session, TextMessage message) {
            try {
                String payload = message.getPayload();
                lastMessageTime = System.currentTimeMillis();
                
                // Handle welcome message from message queue
                if (payload.contains("\"type\":\"welcome\"")) {
                    System.out.println("Connected to message queue successfully");
                    // Start timeout checker after welcome message
                    scheduleTimeoutCheck();
                    return;
                }
                
                HospitalInformation hospitalInformation = objectMapper.readValue(payload, HospitalInformation.class);

                if (hospitalInformation != null) {
                    hospitalInformationList.add(hospitalInformation);
                    System.out.println("Received hospital information from message queue: " + hospitalInformation);
                    // Reset timeout after receiving data
                    scheduleTimeoutCheck();
                }
            } catch (JsonProcessingException e) {
                System.err.println("Error parsing message from message queue: " + e.getMessage());
                System.err.println("Raw message: " + message.getPayload());
            }
        }
        
        private void scheduleTimeoutCheck() {
            // Use a separate thread to check for timeout
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(MESSAGE_TIMEOUT);
                    if (System.currentTimeMillis() - lastMessageTime >= MESSAGE_TIMEOUT && !dataReceivingComplete) {
                        System.out.println("No more messages received for " + MESSAGE_TIMEOUT + "ms. Completing data reception.");
                        completeDataReception();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        private void completeDataReception() {
            if (!dataReceivingComplete) {
                dataReceivingComplete = true;
                System.out.println("Data reception completed. Received " + hospitalInformationList.size() + " hospitals");
                if (webSocketSession != null && webSocketSession.isOpen()) {
                    try {
                        webSocketSession.close();
                    } catch (Exception e) {
                        System.err.println("Error closing WebSocket session: " + e.getMessage());
                    }
                }
                futureResult.complete(Result.success(hospitalInformationList));
            }
        }

        @Override
        public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
            // Once the connection is closed, complete the future with the result if not already completed
            if (!dataReceivingComplete) {
                completeDataReception();
            }
        }
        
        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            webSocketSession = session;
            System.out.println("WebSocket connection to message queue established");
        }
        
        @Override
        public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
            System.err.println("WebSocket transport error: " + exception.getMessage());
            if (!futureResult.isDone()) {
                futureResult.complete(Result.failure("WebSocket transport error: " + exception.getMessage(), 500));
            }
        }
    }
}
