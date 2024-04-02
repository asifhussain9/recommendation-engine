package com.example.recommendationengine.component.valid;

import com.example.recommendationengine.component.RecommendationFactory;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.handler.ProductHandler;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.transformer.ProductTransformer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("discountedProductsRecommendationFactory")
@ConditionalOnProperty(value = "spring.application.properties.recommendation-engine.recommendation-factory.discounted-products.enabled", havingValue = "true")
@RequiredArgsConstructor
public class DiscountedProductsRecommendationFactory implements RecommendationFactory {
    private final ProductHandler productHandler;
    private Logger logger = LoggerFactory.getLogger(DiscountedProductsRecommendationFactory.class);

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        return productHandler.getDiscountedProducts(0.00)
                .map(ProductTransformer::toDTO)
                .collectList()
                .map(products -> {
                    return RecommendationResponseDTO.builder()
                            .recommendationType(RecommendationType.DISCOUNTED_PRODUCTS)
                            .products(products)
                            .userId(recommendationRequestDTO.getUserId())
                            .build();
                }).onErrorResume(throwable -> {
                    logger.error("Error while fetching discounted products: {}", throwable.getMessage());
                    return Mono.empty();
                });
    }
}
