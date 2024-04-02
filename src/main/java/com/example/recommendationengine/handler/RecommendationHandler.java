package com.example.recommendationengine.handler;

import com.example.recommendationengine.component.RecommendationEngine;
import com.example.recommendationengine.component.RecommendationFactory;
import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationsResponseDTO;
import com.example.recommendationengine.model.RecommendationType;
import com.example.recommendationengine.util.SafeGetUtil;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationHandler {
    private final RecommendationEngine recommendationEngine;
    public Mono<RecommendationsResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        if (isInvalidRequest(recommendationRequestDTO)) {
            RecommendationEngineError error = RecommendationEngineError.builder().code("11").message("Invalid request").build();
            return Mono.just(RecommendationsResponseDTO.builder().error(error).build());
        }

        if (SafeGetUtil.resolve(() -> recommendationRequestDTO.getRecommendation()).isPresent()) {
            Optional<RecommendationType> recommendationTypeOpt = Arrays.stream(RecommendationType.values()).filter(recommendationType -> recommendationType.name().equals(recommendationRequestDTO.getRecommendation())).findAny();
            if (recommendationTypeOpt.isPresent()) {
                return recommend(recommendationRequestDTO, recommendationTypeOpt.get());
            }
            return Mono.just(RecommendationsResponseDTO.builder().build());
        }

        return Mono.empty();
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

    private Boolean isInvalidRequest(RecommendationRequestDTO recommendationRequestDTO) {
        boolean aNullRecommendationRequest = Objects.isNull(recommendationRequestDTO);
        boolean blankUserId = StringUtils.isBlank(recommendationRequestDTO.getUserId());
        boolean blankProductId = StringUtils.isBlank(recommendationRequestDTO.getProductId());

        return aNullRecommendationRequest || blankUserId || blankProductId;
    }
}
