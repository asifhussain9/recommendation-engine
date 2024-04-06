package com.example.recommendationengine.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "properties.recommendation-engine")
@EnableConfigurationProperties
@Data
public class Properties {
    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${spring.data.mongodb.uri}")
    private String databaseUri;
    @Value("${spring.data.mongodb.database}")
    private String databaseName;
    private Integer daysLimit;
    private Integer maxProducts;
    private Double minDiscount;
    private Double minRating;
    private Integer pageSize;
    private Integer minQuantitySold;
    private Integer maxStocks;

}
