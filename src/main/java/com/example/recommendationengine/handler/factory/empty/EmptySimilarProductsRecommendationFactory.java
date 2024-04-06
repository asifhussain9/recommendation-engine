package com.example.recommendationengine.handler.factory.empty;

import com.example.recommendationengine.handler.factory.RecommendationFactory;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("similarProductsRecommendationFactory")
@ConditionalOnProperty(value = "properties.recommendation-engine.recommendation-factory.similar-products.enabled", havingValue = "false")
public class EmptySimilarProductsRecommendationFactory implements RecommendationFactory {
    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        return Mono.empty();
    }
}
