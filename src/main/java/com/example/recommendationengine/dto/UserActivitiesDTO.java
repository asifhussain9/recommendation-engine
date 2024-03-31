package com.example.recommendationengine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserActivitiesDTO {
    private String userId;
    private String activity;
    private String productId;
}
