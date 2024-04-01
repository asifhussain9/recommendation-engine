package com.example.recommendationengine.model;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NullProduct extends Product {

    NullProduct(String id, String name, String description, ProductCategory category, String subCategory, String imageUrl, double price, int stock, double discount, double averageRating, int ratingCount, int sold) {
        super(id, name, description, category, subCategory, imageUrl, price, stock, discount, averageRating, ratingCount, sold);
    }

    @Override
    public Boolean isNullProduct() {
        return true;
    }
}
