package com.example.recommendationengine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI baseOpenAPI() {
        Info info = new Info().title("Recommendation Engine OpenAPI Docs").version("1.0.0").description("Doc Description");
        return new OpenAPI().info(info);
    }

}