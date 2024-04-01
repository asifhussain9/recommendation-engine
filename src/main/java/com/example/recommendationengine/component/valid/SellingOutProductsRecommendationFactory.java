package com.example.recommendationengine.component.valid;

import com.example.recommendationengine.component.RecommendationFactory;
import com.example.recommendationengine.dto.request.RecommendationRequestDTO;
import com.example.recommendationengine.dto.response.RecommendationsResponseDTO;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Profile("Prod")
public class SellingOutProductsRecommendationFactory implements RecommendationFactory {
    public Mono<RecommendationsResponseDTO> recommend(RecommendationRequestDTO recommendationRequestDTO) {
        
        return Mono.empty();
    }
}
