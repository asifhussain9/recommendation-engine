package com.example.recommendationengine.repository;

import com.example.recommendationengine.model.Activity;
import com.example.recommendationengine.model.UserActivity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface UserActivityRepository extends ReactiveMongoRepository<UserActivity, String>{
    Flux<UserActivity> findByUserIdAndCreatedAtAfter(String userId, LocalDateTime createdAt, Pageable pageable);
    Flux<UserActivity> findByCreatedAtAfter(LocalDateTime createdAt, Pageable pageable);
    Flux<UserActivity> findByProductIdAndActivityAndCreatedAtAfter(String productId, Activity activity, LocalDateTime createdAt);
    Flux<UserActivity> findByUserIdAndProductIdNotAndActivityAndCreatedAtAfter(String userId, String productId, Activity activity, LocalDateTime createdAt);
}
