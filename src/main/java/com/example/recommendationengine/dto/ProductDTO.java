package com.example.recommendationengine.dto;

import com.example.recommendationengine.model.ProductCategory;
import com.example.recommendationengine.model.ProductReview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private String imageUrl;
    private double price;
    private int stock;
    private double discount;
    private double averageRating;
    private int ratingCount;
    private int sold;
}
