package com.example.recommendationengine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RecommendationRequestDTO {
    private String userId;
    private String productId;
    private String recommendation;
}
