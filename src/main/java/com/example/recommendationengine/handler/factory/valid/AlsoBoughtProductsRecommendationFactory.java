package com.example.recommendationengine.handler.factory.valid;

import com.example.recommendationengine.config.Properties;
import com.example.recommendationengine.handler.factory.RecommendationFactory;
import com.example.recommendationengine.dto.request.ProductDTO;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.model.Activity;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.model.UserActivity;
import com.example.recommendationengine.repository.UserActivityRepository;
import com.example.recommendationengine.transformer.ProductTransformer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Component("alsoBoughtProductsRecommendationFactory")
@ConditionalOnProperty(value = "properties.recommendation-engine.recommendation-factory.also-bought-products.enabled", havingValue = "true")
@RequiredArgsConstructor
public class AlsoBoughtProductsRecommendationFactory implements RecommendationFactory {
    private final UserActivityRepository userActivityRepository;
    private final Logger logger = LoggerFactory.getLogger(AlsoBoughtProductsRecommendationFactory.class);
    private final Properties properties;

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        LocalDateTime tenDaysAgo = LocalDateTime.now(UTC).minusDays(properties.getDaysLimit());
        return userActivityRepository.findByProductIdAndActivityAndCreatedAtAfter(recommendationRequestDTO.getProductId(), Activity.PURCHASE, tenDaysAgo)
                .flatMap(userActivity -> userActivityRepository.findByUserIdAndProductIdNotAndActivityAndCreatedAtAfter(userActivity.getUser().getId(), recommendationRequestDTO.getProductId(), Activity.PURCHASE, tenDaysAgo))
                .map(UserActivity::getProduct)
                .map(ProductTransformer::toDTO)
                .collectList()
                .flatMap(products -> {
                    Map<ProductDTO, Long> productPurchaseFrequencyMap = products.stream()
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                    List<ProductDTO> mostBoughtProducts = productPurchaseFrequencyMap.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue())
                            .limit(properties.getMaxProducts())
                            .map(Map.Entry::getKey)
                            .toList();
                    RecommendationResponseDTO recommendationResponseDTO = RecommendationResponseDTO.builder()
                            .recommendationType(RecommendationType.ALSO_BOUGHT_PRODUCTS)
                            .products(mostBoughtProducts)
                            .userId(recommendationRequestDTO.getUserId())
                            .build();
                    return Mono.just(recommendationResponseDTO);
                }).onErrorResume(throwable -> {
                    logger.error("Error while fetching also bought products: {}", throwable.getMessage());
                    return Mono.empty();
                });
    }
}
