package com.example.recommendationengine.repository;

import com.example.recommendationengine.model.Product;
import com.example.recommendationengine.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
