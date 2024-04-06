package com.example.recommendationengine.handler.factory.valid;

import com.example.recommendationengine.config.Properties;
import com.example.recommendationengine.handler.factory.RecommendationFactory;
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
@ConditionalOnProperty(value = "properties.recommendation-engine.recommendation-factory.high-rated-products.enabled", havingValue = "true")
@RequiredArgsConstructor
public class HighRatedProductsRecommendationFactory implements RecommendationFactory {
    private final ProductHandler productHandler;
    private final Properties properties;

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        return productHandler.getAllProducts().filter(product -> product.getAverageRating() > properties.getMinRating())
                .map(ProductTransformer::toDTO)
                .collectList()
                .map(products -> RecommendationResponseDTO.builder()
                        .recommendationType(RecommendationType.HIGH_RATED_PRODUCTS)
                        .userId(recommendationRequestDTO.getUserId())
                        .products(products)
                        .build());
    }
}
