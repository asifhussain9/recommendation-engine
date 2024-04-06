package com.example.recommendationengine.controller;

import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.request.ProductDTO;
import com.example.recommendationengine.dto.response.ProductResponseDTO;
import com.example.recommendationengine.dto.response.UserResponseDTO;
import com.example.recommendationengine.handler.ProductHandler;
import com.example.recommendationengine.transformer.ProductTransformer;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@Data
@AllArgsConstructor
public class ProductController {
    private ProductHandler productHandler;

    @Bean
    public RouterFunction<ServerResponse> routeProduct() {
        return RouterFunctions.route()
                .POST("/product", this::createProduct)
                .GET("/products", this::getAllProducts)
                .POST("/product/{id}", this::updateProduct)
                .GET("/product/{id}", this::getProduct)
                .DELETE("/product/{id}", this::deleteProduct)
                .build();
    }

    private Mono<ServerResponse> createProduct(ServerRequest request) {
        return productHandler.createProduct(request.bodyToMono(ProductDTO.class))
                .flatMap(product -> ServerResponse.ok().body(Mono.just(createProductResponseDTO(List.of(ProductTransformer.toDTO(product)))), ProductResponseDTO.class))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> getAllProducts(ServerRequest request) {
        return productHandler.getAllProducts()
                .map(ProductTransformer::toDTO)
                .collectList()
                .flatMap(productDTOS -> ServerResponse.ok().body(Flux.just(createProductResponseDTO(productDTOS)), ProductResponseDTO.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> getProduct(ServerRequest request) {
        return productHandler.getProduct(request.pathVariable("id"))
                .map(ProductTransformer::toDTO)
                .flatMap(productDTO -> ServerResponse.ok().body(Mono.just(createProductResponseDTO(List.of(productDTO))), ProductResponseDTO.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> updateProduct(ServerRequest request) {
        return productHandler.updateProduct(request.pathVariable("id"), request.bodyToMono(ProductDTO.class))
                .flatMap(product -> ServerResponse.ok().body(Mono.just(createProductResponseDTO(List.of(ProductTransformer.toDTO(product)))), ProductResponseDTO.class))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        return productHandler.deleteProduct(serverRequest.pathVariable("id"))
                .then(ServerResponse.noContent().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }
    private ProductResponseDTO createProductResponseDTO(List<ProductDTO> productDTOs) {
        return ProductResponseDTO.builder()
                .products(productDTOs)
                .build();
    }

    private UserResponseDTO buildErrorResponse(RecommendationEngineError error) {
        return UserResponseDTO.builder()
                .error(error)
                .build();
    }

}
