package com.example.recommendationengine.model;

import java.time.LocalDateTime;

public class ProductReview {
    private User user;
    private Product product;
    private String review;
    private double rating;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
