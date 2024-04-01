package com.example.recommendationengine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private ProductCategory category;
    private String subCategory;
    private String imageUrl;
    private double price;
    private int stock;
    private double discount;
    private double averageRating;
    private int ratingCount;
    private int sold;

    public Boolean isNullProduct() {
        return false;
    }
}
