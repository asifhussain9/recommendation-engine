package com.example.recommendationengine.consumer;

import lombok.NoArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class UserActivityConsumer {
    @KafkaListener(topics = "user.activities", groupId = "recommendation-engine")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
    }
}
