package com.example.recommendationengine.handler.factory;

import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import reactor.core.publisher.Mono;

public interface RecommendationFactory {
    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO);
}
