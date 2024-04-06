package com.example.recommendationengine.handler.factory.valid;

import com.example.recommendationengine.config.Properties;
import com.example.recommendationengine.handler.factory.RecommendationFactory;
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

@Component
@ConditionalOnProperty(value = "properties.recommendation-engine.recommendation-factory.recently-viewed-products.enabled", havingValue = "true")
@RequiredArgsConstructor
public class RecentlyViewedProductsRecommendationFactory implements RecommendationFactory {
    private final UserActivityRepository userActivityRepository;
    private final Properties properties;
    private final Logger logger = LoggerFactory.getLogger(RecentlyViewedProductsRecommendationFactory.class);

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        LocalDateTime tenDaysAgo = LocalDateTime.now(UTC).minusDays(properties.getDaysLimit());
        Pageable topTen = PageRequest.of(0, properties.getPageSize());
        return userActivityRepository.findByUserIdAndCreatedAtAfter(recommendationRequestDTO.getUserId(), tenDaysAgo, topTen)
                .map(UserActivity::getProduct)
                .map(ProductTransformer::toDTO)
                .collectList()
                .flatMap(products -> {
                    RecommendationResponseDTO recommendationResponseDTO = RecommendationResponseDTO.builder()
                            .recommendationType(RecommendationType.RECENTLY_VIEWED_PRODUCTS)
                            .products(products)
                            .userId(recommendationRequestDTO.getUserId())
                            .build();
                    return Mono.just(recommendationResponseDTO);
                }).onErrorResume(throwable -> {
                    logger.error("Error while fetching recently viewed products: {}", throwable.getMessage());
                    return Mono.empty();
                });
    }
}
