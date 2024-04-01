package com.example.recommendationengine.dto.response;

import com.example.recommendationengine.dto.RecommendationEngineError;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class RecommendationsResponseDTO {
    RecommendationEngineError error;
    List<RecommendationResponseDTO> recommendationResponseDTO;
}
