package com.example.recommendationengine.handler.factory.empty;

import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.handler.ProductHandler;
import com.example.recommendationengine.handler.factory.RecommendationFactory;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.transformer.ProductTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("highRatedProductsRecommendationFactory")
@ConditionalOnProperty(value = "properties.recommendation-engine.recommendation-factory.high-rated-products.enabled", havingValue = "false")
@RequiredArgsConstructor
public class EmptyHighRatedProductsRecommendationFactory implements RecommendationFactory {
    private final ProductHandler productHandler;

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        return Mono.empty();
    }
}
