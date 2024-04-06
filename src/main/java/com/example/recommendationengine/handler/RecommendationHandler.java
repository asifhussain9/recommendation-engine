package com.example.recommendationengine.handler;

import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationResponseDTO;
import com.example.recommendationengine.dto.response.RecommendationsResponseDTO;
import com.example.recommendationengine.handler.factory.RecommendationEngine;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.util.SafeGetUtil;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RecommendationHandler {
    private final RecommendationEngine recommendationEngine;
    private final Logger logger = LoggerFactory.getLogger(RecommendationHandler.class);
    public Mono<RecommendationsResponseDTO> createRecommendations(RecommendationRequestDTO recommendationRequestDTO) {
        if (isInvalidRequest(recommendationRequestDTO)) {
            RecommendationEngineError error = RecommendationEngineError.builder().code("11").message("Invalid request").build();
            return Mono.just(RecommendationsResponseDTO.builder().error(error).build());
        }

        if (SafeGetUtil.resolve(() -> recommendationRequestDTO.getRecommendation()).isPresent()) {
            Optional<RecommendationType> recommendationTypeOpt = Arrays.stream(RecommendationType.values()).filter(recommendationType -> recommendationType.name().equals(recommendationRequestDTO.getRecommendation())).findAny();
            if (recommendationTypeOpt.isPresent()) {
                return recommend(recommendationRequestDTO, recommendationTypeOpt.get());
            }
            logger.error("Invalid recommendation type: {}", recommendationRequestDTO.getRecommendation());
            return Mono.empty();
        }

        return recommend(recommendationRequestDTO);
    }

    private Mono<RecommendationsResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO, RecommendationType recommendationType) {
        RecommendationsResponseDTO.RecommendationsResponseDTOBuilder builder = RecommendationsResponseDTO.builder();
        return recommendationEngine.getRecommendationFactory(recommendationType).recommend(recommendationRequestDTO)
                .map(recommendationResponseDTO -> builder.recommendationResponseDTO(List.of(recommendationResponseDTO)).build())
                .onErrorResume(throwable -> {
                    RecommendationEngineError error = RecommendationEngineError.builder().code("12").message("Error while fetching recommendation").build();
                    return Mono.just(builder.error(error).build());
                });
    }

    private Mono<RecommendationsResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        RecommendationsResponseDTO.RecommendationsResponseDTOBuilder builder = RecommendationsResponseDTO.builder();
        var recommendations = new ArrayList<RecommendationResponseDTO>();
        Mono<List<Boolean>> fetchingRecommendation = createFetchingRecommendation(recommendationRequestDTO, recommendations);
        return handleFetchingRecommendationResult(builder, recommendations, fetchingRecommendation);
    }

    private Mono<List<Boolean>> createFetchingRecommendation(RecommendationRequestDTO recommendationRequestDTO, List<RecommendationResponseDTO> recommendations) {
        return Flux.fromIterable(List.of(RecommendationType.values()))
                        .flatMap(recommendationType -> createRecommendation(recommendationRequestDTO, recommendations, recommendationType))
                        .collectList();
    }

    private Mono<Boolean> createRecommendation(RecommendationRequestDTO recommendationRequestDTO, List<RecommendationResponseDTO> recommendations, RecommendationType recommendationType) {
        return recommendationEngine.getRecommendationFactory(recommendationType).recommend(recommendationRequestDTO)
                .map(recommendations::add)
                .onErrorResume(throwable -> Mono.just(false));
    }

    private Mono<RecommendationsResponseDTO> handleFetchingRecommendationResult(RecommendationsResponseDTO.RecommendationsResponseDTOBuilder builder, List<RecommendationResponseDTO> recommendations, Mono<List<Boolean>> fetchingRecommendation) {
        return fetchingRecommendation.flatMap(fetchingRecommendationResult -> {
            if (fetchingRecommendationResult.stream().allMatch(Boolean::booleanValue)) {
                return Mono.just(builder.recommendationResponseDTO(recommendations).build());
            }
            RecommendationEngineError error = RecommendationEngineError.builder().code("12").message("Error while fetching recommendation").build();
            return Mono.just(builder.error(error).build());
        });
    }

    private Boolean isInvalidRequest(RecommendationRequestDTO recommendationRequestDTO) {
        boolean aNullRecommendationRequest = Objects.isNull(recommendationRequestDTO);
        boolean blankUserId = StringUtils.isBlank(recommendationRequestDTO.getUserId());
        boolean blankProductId = StringUtils.isBlank(recommendationRequestDTO.getProductId());

        return aNullRecommendationRequest || blankUserId || blankProductId;
    }
}
