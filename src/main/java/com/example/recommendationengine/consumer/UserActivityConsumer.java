package com.example.recommendationengine.consumer;

import com.example.recommendationengine.dto.request.UserActivitiesDTO;
import com.example.recommendationengine.handler.ProductHandler;
import com.example.recommendationengine.handler.UserHandler;
import com.example.recommendationengine.model.*;
import com.example.recommendationengine.repository.UserActivityRepository;
import com.example.recommendationengine.transformer.UserActivityTransformer;
import com.example.recommendationengine.util.SafeGetUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor
public class UserActivityConsumer {
    private final UserActivityRepository userActivityRepository;
    private final UserActivityTransformer userActivityTransformer;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(UserActivityConsumer.class);

    @KafkaListener(topics = "user.activities", groupId = "recommendation-engine")
    public void consume(String message) {
        logger.info("Consuming message: " + message);
        try {
            UserActivitiesDTO userActivitiesDTO = objectMapper.readValue(message, UserActivitiesDTO.class);
            userActivityTransformer.transform(userActivitiesDTO)
                    .flatMap(userActivityRepository::save)
                    .doOnError(throwable -> logger.error("Error while saving user activity: {}, error {}", userActivitiesDTO.toString(), throwable.getMessage()))
                    .doFinally(tuple -> logger.info("Consumed message: {}", userActivitiesDTO))
                    .subscribe();
        } catch (JsonProcessingException e) {
            logger.error("Error while processing message: {}", e.getMessage());
        }
    }
}
