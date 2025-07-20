package com.onlybuns.onlybuns.domain.services;

import com.onlybuns.onlybuns.core.dto.PostAdvertisementDto;
import com.onlybuns.onlybuns.infrastructure.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdvertisementService {

    private final RabbitTemplate rabbitTemplate;

    public void sendPostAdvertisement(PostAdvertisementDto advertisementDto) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.POST_ADVERTISEMENTS_EXCHANGE,
                "",  // Routing key is empty for fanout exchange
                advertisementDto
            );
            log.info("Successfully sent advertisement for post by user: {}", advertisementDto.getUsername());
        } catch (Exception e) {
            log.error("Failed to send advertisement for post by user: {}. Error: {}", 
                     advertisementDto.getUsername(), e.getMessage());
            // You could add retry logic here or queue for later processing
        }
    }
}
