# Recommendation Engine
Recommendation engine is an E-commerce application that recommends relevant products to users based on their browsing history

# Overview
A basic recommendation system for an e-commerce website using a reactive architecture.
This system should personalize product suggestions for users based on their recent browsing behavior.

## Focus
Real-time User Interaction Tracking: Capture user actions like product views and clicks on the website.

Simple Recommendation Engine: Recommend products similar to those a user has recently viewed.

Improved User Experience: Provide relevant product suggestions to increase user engagement.

Technology Stack:
Spring Boot: Build a lightweight microservice for processing user interactions.
WebFlux: Handle real-time data processing with a reactive approach.
MongoDB: Store user profiles and browsing history data.
(Optional) Redis: Cache frequently accessed product data for faster recommendations (can be omitted for a simpler version).

## Goals
Develop a Spring Boot application with a WebFlux API to capture user interactions.
Application consumes events from Kafka for each user activity.
Implement logic to recommend products similar to those a user has recently viewed based on data stored in MongoDB.
(Optional) Integrate Redis for caching product data to improve recommendation speed.
This project will introduce you to the concepts of reactive programming and building a microservice with Spring Boot and WebFlux.
It will provide a foundation for you to explore more advanced recommendation systems in the future.

# Objects Identified
## User
- id
- name
- email
- password
- created_at
- updated_at
- last_login

## Product
- id
- name
- description
- category
- brand
- price
- discount
- created_at
- updated_at
- average_rating
- reviews

## ProductCategory
- id
- name
- description
- created_at
- updated_at

## Brand
- id
- name
- description
- created_at
- updated_at

## ProductReview
- id
- product_id
- user_id
- rating
- review
- created_at
- updated_at

## UserActivity
- id
- user_id
- product_id
- activity_type
- created_at
- updated_at

## UserActivityType
- id
- name
- description
- created_at
- updated_at

## Cart
- id
- user_id
- cart_items
- created_at
- updated_at

## CartItem
- id
- product_id
- quantity

## Wishlist
- id
- user_id
- wishlist_items
- created_at
- updated_at

## Recommendation
- id
- user_id
- recommended_products
- recommendation_type
- created_at
- updated_at

## RecommendationType
- id
- name
- description
- created_at
- updated_at

# MongoDB Schema
## User
```json
{
    "id": "string",
    "name": "string",
    "email": "string",
    "password": "string",
    "created_at": "date",
    "updated_at": "date",
    "last_login": "date"
}
```

# API Endpoints
# Edge cases