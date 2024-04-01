package com.example.recommendationengine.config;

import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationsResponseDTO;
import com.example.recommendationengine.handler.RecommendationHandler;
import com.example.recommendationengine.model.Recommendation;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@Data
@AllArgsConstructor
public class RecommendationController {
    private final RecommendationHandler recommendationHandler;
    private final Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    @Bean
    public RouterFunction<ServerResponse> routeRecommendation() {
        return RouterFunctions.route()
                .GET("/recommendation", this::recommend)
                .build();
    }

    private Mono<ServerResponse> recommend(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RecommendationRequestDTO.class)
                .flatMap(recommendationRequestDTO -> {
                    Mono<RecommendationsResponseDTO> recommendationsResponseDTOMono = recommendationHandler.recommend(recommendationRequestDTO);
                    return ServerResponse.ok().body(recommendationsResponseDTOMono, RecommendationsResponseDTO.class);
                }).switchIfEmpty(Mono.defer(() -> {
                    RecommendationEngineError error = RecommendationEngineError.builder().code("2").message("No recommendations found").build();
                    RecommendationsResponseDTO recommendations = RecommendationsResponseDTO.builder().error(error).build();
                    return ServerResponse.badRequest().bodyValue(recommendations);
                })).onErrorResume(throwable -> {
                    logger.error("Error occurred while processing the request", throwable);
                    RecommendationEngineError error = RecommendationEngineError.builder().code("10").message("Error occurred while processing the request").build();
                    RecommendationsResponseDTO recommendations = RecommendationsResponseDTO.builder().error(error).build();
                    return ServerResponse.badRequest().bodyValue(recommendations);
                });

    }
}
