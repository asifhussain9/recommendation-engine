package com.example.recommendationengine.component;

import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.dto.response.RecommendationsResponseDTO;
import reactor.core.publisher.Mono;

public interface RecommendationFactory {
    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO);
}
