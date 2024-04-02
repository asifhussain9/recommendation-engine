package com.example.recommendationengine.component.valid;

import com.example.recommendationengine.component.RecommendationFactory;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.handler.ProductHandler;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.transformer.ProductTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("highRatedProductsRecommendationFactory")
@ConditionalOnProperty(value = "spring.application.properties.recommendation-engine.recommendation-factory.high-rated-products.enabled", havingValue = "true")
@RequiredArgsConstructor
public class HighRatedProductsRecommendationFactory implements RecommendationFactory {
    private final ProductHandler productHandler;

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        return productHandler.getAllProducts().filter(product -> product.getAverageRating() > 4.5)
                .map(ProductTransformer::toDTO)
                .collectList()
                .map(products -> RecommendationResponseDTO.builder()
                        .recommendationType(RecommendationType.HIGH_RATED_PRODUCTS)
                        .userId(recommendationRequestDTO.getUserId())
                        .products(products)
                        .build());
    }
}
