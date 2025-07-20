package com.onlybuns.onlybuns.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.HospitalInformation;
import com.onlybuns.onlybuns.domain.serviceinterfaces.HospitalServiceInterface;

@Service
public class HospitalService implements HospitalServiceInterface {

    private final String messageQueueUrl = "http://message-queue:4000";
    private final String vetsAndPetsUrl = "http://vets-and-pets-be:3000";
    private final String queueName = "hospital-data";

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Result<List<HospitalInformation>> connectToNodeServer() {
        try {
            // First, trigger Vets&Pets to send data to message queue
            triggerVetsToSendData();
            
            // Give some time for Vets&Pets to send all data
            Thread.sleep(2000);
            
            // Then consume all messages from message queue
            List<HospitalInformation> hospitalInformationList = consumeMessagesFromQueue();
            
            return Result.success(hospitalInformationList);
        } catch (Exception e) {
            return Result.failure("Failed to fetch hospital data: " + e.getMessage(), 500);
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

    private List<HospitalInformation> consumeMessagesFromQueue() {
        List<HospitalInformation> hospitalInformationList = new ArrayList<>();
        
        try {
            HttpClient client = HttpClient.newHttpClient();
            
            // Keep consuming until no more messages
            boolean hasMoreMessages = true;
            int maxRetries = 10; // Prevent infinite loop
            int retryCount = 0;
            
            while (hasMoreMessages && retryCount < maxRetries) {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(messageQueueUrl + "/api/queue/" + queueName + "/consume?limit=50"))
                        .GET()
                        .header("Content-Type", "application/json")
                        .build();
                        
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                
                if (response.statusCode() == 200) {
                    JsonNode responseNode = objectMapper.readTree(response.body());
                    JsonNode messagesNode = responseNode.get("messages");
                    
                    if (messagesNode != null && messagesNode.isArray() && messagesNode.size() > 0) {
                        for (JsonNode messageNode : messagesNode) {
                            try {
                                HospitalInformation hospitalInfo = objectMapper.readValue(messageNode.toString(), HospitalInformation.class);
                                hospitalInformationList.add(hospitalInfo);
                                System.out.println("Consumed hospital information from message queue: " + hospitalInfo);
                            } catch (JsonProcessingException e) {
                                System.err.println("Error parsing message from queue: " + e.getMessage());
                                System.err.println("Raw message: " + messageNode.toString());
                            }
                        }
                        
                        // Check if there might be more messages
                        hasMoreMessages = messagesNode.size() == 50; // If we got max limit, there might be more
                    } else {
                        hasMoreMessages = false;
                    }
                } else {
                    System.err.println("Failed to consume messages from queue: " + response.statusCode() + " - " + response.body());
                    hasMoreMessages = false;
                }
                
                retryCount++;
                
                // Small delay between requests
                if (hasMoreMessages) {
                    Thread.sleep(100);
                }
            }
            
            System.out.println("Finished consuming messages. Total received: " + hospitalInformationList.size());
            
        } catch (Exception e) {
            System.err.println("Error consuming messages from queue: " + e.getMessage());
        }
        
        return hospitalInformationList;
    }
}
