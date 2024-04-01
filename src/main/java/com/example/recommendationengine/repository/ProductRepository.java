package com.example.recommendationengine.repository;

import com.example.recommendationengine.model.Product;
import com.example.recommendationengine.model.ProductCategory;
import com.example.recommendationengine.model.User;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
    Flux<Product> findByCategoryAndSubCategoryAndIdNot(ProductCategory category, String subCategory, String id);

}
