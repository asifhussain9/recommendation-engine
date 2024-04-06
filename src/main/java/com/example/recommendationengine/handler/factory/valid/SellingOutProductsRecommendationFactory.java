package com.example.recommendationengine.handler.factory.valid;

import com.example.recommendationengine.config.Properties;
import com.example.recommendationengine.handler.factory.RecommendationFactory;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.repository.ProductRepository;
import com.example.recommendationengine.transformer.ProductTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component("sellingOutProductsRecommendationFactory")
@ConditionalOnProperty(value = "properties.recommendation-engine.recommendation-factory.selling-out-products.enabled", havingValue = "true")
@RequiredArgsConstructor
public class SellingOutProductsRecommendationFactory implements RecommendationFactory {
    private final ProductRepository productRepository;
    private final Properties properties;

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        Pageable topTen = PageRequest.of(0, properties.getPageSize());
        return productRepository.findBySoldGreaterThanAndStockLessThan(properties.getMinQuantitySold(), properties.getMaxStocks(), topTen)
                .map(ProductTransformer::toDTO)
                .collectList()
                .flatMap(products -> {
                    RecommendationResponseDTO recommendationResponseDTO = RecommendationResponseDTO.builder()
                            .recommendationType(RecommendationType.SELLING_OUT_PRODUCTS)
                            .products(products)
                            .userId(recommendationRequestDTO.getUserId())
                            .build();
                    return Mono.just(recommendationResponseDTO);
                });
    }
}
