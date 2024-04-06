package com.example.recommendationengine.handler.factory.empty;

import com.example.recommendationengine.dto.request.ProductDTO;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.handler.factory.RecommendationFactory;
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
@ConditionalOnProperty(value = "properties.recommendation-engine.recommendation-factory.also-bought-products.enabled", havingValue = "false")
@RequiredArgsConstructor
public class EmptyAlsoBoughtProductsRecommendationFactory implements RecommendationFactory {

    public Mono<RecommendationResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        return Mono.empty();
    }
}
