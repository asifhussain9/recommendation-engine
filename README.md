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

# How to Run
Please check HELP.md for instructions on how to run the application.

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
## users
```json
{
    "id": "string",
    "name": "string",
    "email": "string",
    "password": "string",
    "created_at": "date",
    "updated_at": "date",
    "last_login": "date",
    "is_active": "boolean"
}
```
## products
```json
{
    "id": "string",
    "name": "string",
    "description": "string",
    "category": "string",
    "image_url": "string",
    "price": "number",
    "stock": "number",
    "discount": "number",
    "created_at": "date",
    "updated_at": "date",
    "average_rating": "number",
    "rating_count": "number"
}
```
## user_activities
```json
{
    "id": "string",
    "user": "object",
    "product": "object",
    "activity": "string",
    "created_at": "date",
    "updated_at": "date"
}
```

# API Endpoints
## User
- POST /users ## Create a new user
- GET /users ## Get all users
- GET /users/{id} ## Get user by id
- PATCH /users/{id} ## Update user by id
- DELETE /users/{id} ## Delete user by id

# Recommendation types
Following are the types of recommendations that this application will provide
- Similar products: Recommend products similar to those a user has recently viewed/purchased. This filters out products based on category and sub category.
- Also bought products: Recommend products that other users have bought along with the products a user has recently viewed/purchased.
- Popular/Trending products: Recommend products that are currently trending on the platform.
- Selling out products: Recommend products that are running out of stock.
- High rated products: Recommend products that have high ratings.
- Discounted products: Recommend products that are currently on discount.
- Recently viewed products: Recommend products that a user has recently viewed.

# Edge cases
- What if I want to disable all/specific recommendations?
- Some product recommendations require reading user activities. There should be a limit to how old a user activity can be to be considered for recommendations.
- What if a user deletes their account? Should we delete all their activities and recommendations?
- For high rated products, there should be a minimum rating threshold.
- For discounted products, there should be a minimum discount percentage.
- For selling out products, there should be a minimum stock threshold.
- For also bought products, there should be a minimum number of users who have bought the products together.
- What if multiple pods update the same user/product at the same time?

## Assumptions for above edge cases
- There will be a configuration to enable/disable each recommendation type. This comes from application.yaml.
- There will be a configuration to set the maximum age of user activities to be considered for recommendations. This comes from application.yaml.
- When a user deletes their account, their activities and recommendations will not be deleted. Instead, user is marked as inactive. We will filter only active users/products/activities.
- For high rated products, there will be a minimum rating threshold. This comes from application.yaml.
- For discounted products, there will be a minimum discount percentage. This comes from application.yaml.
- For selling out products, there will be a minimum stock threshold. This comes from application.yaml.
- For also bought products, there will be a minimum number of users who have bought the products together. This comes from application.yaml.
- We will use atomic update operation to handle concurrent updates to the same user/product.

# High level design
![RecommendationEngine drawio](https://github.com/asifhussain9/recommendation-engine/assets/68184577/23f9e6ee-72ee-4e6a-9e05-01e52f4cf512)



