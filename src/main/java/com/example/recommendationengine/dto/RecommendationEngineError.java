package com.example.recommendationengine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendationEngineError extends RuntimeException {
    private String code;
    private String message;
    private String traceId;
}
