package com.example.recommendationengine.component.empty;

import com.example.recommendationengine.component.RecommendationFactory;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.dto.response.RecommendationsResponseDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("similarProductsRecommendationFactory")
@ConditionalOnProperty(value = "spring.application.properties.recommendation-engine.recommendation-factory.similar-products.enabled", havingValue = "false")
public class EmptySimilarProductsRecommendationFactory implements RecommendationFactory {
    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        return Mono.empty();
    }
}
