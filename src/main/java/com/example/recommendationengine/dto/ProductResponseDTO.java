package com.example.recommendationengine.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProductResponseDTO {
    RecommendationEngineError error;
    List<ProductDTO> products;
}
