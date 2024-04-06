package com.example.recommendationengine.config;

import com.mongodb.reactivestreams.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@Configuration
@RequiredArgsConstructor
public class MongoConfig {
    private final Properties properties;
    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        SimpleReactiveMongoDatabaseFactory factory = new SimpleReactiveMongoDatabaseFactory(MongoClients.create(properties.getDatabaseUri()), properties.getDatabaseName());
        return new ReactiveMongoTemplate(factory);
    }
}
