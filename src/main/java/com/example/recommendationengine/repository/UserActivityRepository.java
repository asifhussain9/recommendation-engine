package com.example.recommendationengine.repository;

import com.example.recommendationengine.model.UserActivity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface UserActivityRepository extends ReactiveMongoRepository<UserActivity, String>{
}
