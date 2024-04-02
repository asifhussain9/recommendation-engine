package com.example.recommendationengine.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Document("user_activities")
@Data
public class UserActivity {
    private User user;
    private Activity activity;
    private Product product;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
