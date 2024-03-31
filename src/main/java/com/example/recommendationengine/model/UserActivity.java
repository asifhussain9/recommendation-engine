package com.example.recommendationengine.model;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;

@Builder
@Document("user_activities")
public class UserActivity {
    @DocumentReference
    private User user;
    private Activity activity;
    @DocumentReference
    private Product product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
