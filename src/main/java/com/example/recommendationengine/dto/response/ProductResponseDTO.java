package com.example.recommendationengine.dto.response;

import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.request.ProductDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProductResponseDTO {
    RecommendationEngineError error;
    List<ProductDTO> products;
}
