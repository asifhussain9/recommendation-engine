package com.example.recommendationengine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductDTO {
    private String id;
    private String name;
    private String description;
    private String category;
    private String subCategory;
    private String imageUrl;
    private double price;
    private int stock;
    private double discount;
    private double averageRating;
    private int ratingCount;
    private int sold;
}
