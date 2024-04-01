package com.example.recommendationengine.transformer;

import com.example.recommendationengine.dto.request.ProductDTO;
import com.example.recommendationengine.model.Product;

import java.util.UUID;

public class ProductTransformer {

    public static ProductDTO toDTO(Product product) {
        String id = product.getId() == null ? UUID.randomUUID().toString() : product.getId();
        return ProductDTO.builder()
                .id(id)
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory().name())
                .subCategory(product.getSubCategory())
                .averageRating(product.getAverageRating())
                .sold(product.getSold())
                .stock(product.getStock())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .ratingCount(product.getRatingCount())
                .discount(product.getDiscount())
                .build();
    }
}
