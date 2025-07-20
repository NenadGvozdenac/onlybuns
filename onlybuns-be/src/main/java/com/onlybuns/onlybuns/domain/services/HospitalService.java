package com.onlybuns.onlybuns.domain.services;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.onlybuns.onlybuns.core.misc.Result;
import com.onlybuns.onlybuns.domain.models.AddressInformation;
import com.onlybuns.onlybuns.domain.models.HospitalInformation;
import com.onlybuns.onlybuns.domain.serviceinterfaces.HospitalServiceInterface;

@Service
public class HospitalService implements HospitalServiceInterface {

    private String messageQueueUrl = "http://message-queue:4000";

    private String vetsAndPetsUrl = "http://vets-and-pets-be:3000";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Result<List<HospitalInformation>> connectToNodeServer() {
        try {
            triggerVetsToSendData();

            // Treba vremena da se saceka da se poruke posalju u queue
            Thread.sleep(2000);

            // Dobavljanje podataka iz queue
            List<HospitalInformation> hospitalData = consumeMessagesFromQueue();

            // Vraca rezultat sa listom bolnica
            return Result.success(hospitalData);
        } catch (Exception e) {
            return Result.failure("Failed to fetch hospital data: " + e.getMessage(), 500);
        }
    }

    private void triggerVetsToSendData() {
        try {
            String url = vetsAndPetsUrl + "/api/send-vets-to-queue";
            restTemplate.postForObject(url, null, String.class);
            System.out.println("Successfully triggered Vets&Pets to send data to message queue");
        } catch (Exception e) {
            System.err.println("Error triggering Vets&Pets to send data: " + e.getMessage());
        }
    }

    private List<HospitalInformation> consumeMessagesFromQueue() {
        try {
            String url = messageQueueUrl + "/api/queue/hospital-data/consume?limit=50";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("messages")) {
                return List.of();
            }

            List<Map<String, Object>> messages = (List<Map<String, Object>>) response.get("messages");

            return messages.stream()
                .map(message -> mapToHospitalInformation(message))
                .toList();

        } catch (Exception e) {
            System.err.println("Error consuming messages from queue: " + e.getMessage());
            return List.of();
        }
    }

    private HospitalInformation mapToHospitalInformation(Map<String, Object> messageData) {
        HospitalInformation hospital = new HospitalInformation();
        hospital.setId((String) messageData.get("_id"));
        hospital.setName((String) messageData.get("name"));
        hospital.setDescription((String) messageData.get("description"));

        AddressInformation location = new AddressInformation();

        Map<String, Object> locationData = (Map<String, Object>) messageData.get("location");

        location.setLatitude(Double.valueOf(locationData.get("latitude").toString()));
        location.setLongitude(Double.valueOf(locationData.get("longitude").toString()));

        hospital.setLocation(location);

        return hospital;
    }
}
