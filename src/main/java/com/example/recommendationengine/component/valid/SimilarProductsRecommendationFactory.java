package com.example.recommendationengine.component.valid;

import com.example.recommendationengine.component.RecommendationFactory;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.dto.response.RecommendationsResponseDTO;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.repository.ProductRepository;
import com.example.recommendationengine.transformer.ProductTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component("similarProductsRecommendationFactory")
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.application.properties.recommendation-engine.recommendation-factory.similar-products.enabled", havingValue = "true")
public class SimilarProductsRecommendationFactory implements RecommendationFactory {
    private final ProductRepository productRepository;
    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        RecommendationsResponseDTO.RecommendationsResponseDTOBuilder finalResponseDTOBuilder = RecommendationsResponseDTO.builder();
        return productRepository.findById(recommendationRequestDTO.getProductId())
                .flatMapMany(product -> productRepository.findByCategoryAndSubCategoryAndIdNot(product.getCategory(), product.getSubCategory(), recommendationRequestDTO.getProductId()))
                .map(ProductTransformer::toDTO)
                .collectList()
                .flatMap(products -> {
                    RecommendationResponseDTO recommendationResponseDTO = RecommendationResponseDTO.builder()
                            .userId(recommendationRequestDTO.getUserId())
                            .products(products)
                            .recommendationType(RecommendationType.SIMILAR_PRODUCTS)
                            .build();
                    return Mono.just(recommendationResponseDTO);
                });
    }
}
