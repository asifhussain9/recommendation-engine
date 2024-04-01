package com.example.recommendationengine.dto.response;

import com.example.recommendationengine.dto.request.ProductDTO;
import com.example.recommendationengine.model.RecommendationType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecommendationResponseDTO {
    private String userId;
    private List<ProductDTO> products;
    private RecommendationType recommendationType;
}
