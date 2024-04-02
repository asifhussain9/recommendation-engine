package com.example.recommendationengine.component.valid;

import com.example.recommendationengine.component.RecommendationFactory;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.model.UserActivity;
import com.example.recommendationengine.repository.UserActivityRepository;
import com.example.recommendationengine.transformer.ProductTransformer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;

@Component("popularProductsRecommendationFactory")
@ConditionalOnProperty(value = "spring.application.properties.recommendation-engine.recommendation-factory.popular-products.enabled", havingValue = "true")
@RequiredArgsConstructor
public class PopularProductsRecommendationFactory implements RecommendationFactory {
    private final UserActivityRepository userActivityRepository;
    private Logger logger = LoggerFactory.getLogger(PopularProductsRecommendationFactory.class);

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        LocalDateTime tenDaysAgo = LocalDateTime.now(UTC).minusDays(10);
        Pageable topTen = PageRequest.of(0, 10);
        return userActivityRepository.findByCreatedAtAfter(tenDaysAgo, topTen)
                .map(UserActivity::getProduct)
                .map(ProductTransformer::toDTO)
                .collectList()
                .flatMap(products -> {
                    RecommendationResponseDTO recommendationResponseDTO = RecommendationResponseDTO.builder()
                            .recommendationType(RecommendationType.POPULAR_PRODUCTS)
                            .products(products)
                            .userId(recommendationRequestDTO.getUserId())
                            .build();
                    return Mono.just(recommendationResponseDTO);
                }).onErrorResume(throwable -> {
                    logger.error("Error while fetching popular products: {}", throwable.getMessage());
                    return Mono.empty();
                });
    }
}
