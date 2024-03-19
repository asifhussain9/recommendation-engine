package com.example.recommendationengine.model;

import java.util.List;

public class Product {
    private String id;
    private String name;
    private String description;
    private ProductCategory category;
    private String imageUrl;
    private double price;
    private int stock;
    private double discount;
    private double averageRating;
    private int ratingCount;
    private int sold;
    private List<ProductReview> reviews;
}
