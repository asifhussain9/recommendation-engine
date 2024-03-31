package com.example.recommendationengine.config;

import com.example.recommendationengine.dto.ProductDTO;
import com.example.recommendationengine.dto.ProductResponseDTO;
import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.UserResponseDTO;
import com.example.recommendationengine.handler.ProductHandler;
import com.example.recommendationengine.model.Product;
import com.example.recommendationengine.model.User;
import com.example.recommendationengine.util.SafeGetUtil;
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
import java.util.UUID;

@Configuration
@Data
@AllArgsConstructor
public class ProductConfiguration {
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
                .flatMap(product -> ServerResponse.ok().body(Mono.just(createProductResponseDTO(List.of(createProductDTO(product)))), ProductResponseDTO.class))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> getAllProducts(ServerRequest request) {
        return productHandler.getAllProducts()
                .map(this::createProductDTO)
                .collectList()
                .flatMap(productDTOS -> ServerResponse.ok().body(Flux.just(createProductResponseDTO(productDTOS)), ProductResponseDTO.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> getProduct(ServerRequest request) {
        return productHandler.getProduct(request.pathVariable("id"))
                .map(this::createProductDTO)
                .flatMap(productDTO -> ServerResponse.ok().body(Mono.just(createProductResponseDTO(List.of(productDTO))), ProductResponseDTO.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> updateProduct(ServerRequest request) {
        return productHandler.updateProduct(request.pathVariable("id"), request.bodyToMono(ProductDTO.class))
                .flatMap(product -> ServerResponse.ok().body(Mono.just(createProductResponseDTO(List.of(createProductDTO(product)))), ProductResponseDTO.class))
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

    private ProductDTO createProductDTO(Product product) {
        String id = product.getId() == null ? UUID.randomUUID().toString() : product.getId();
        return ProductDTO.builder()
                        .id(id)
                        .name(product.getName())
                        .description(product.getDescription())
                        .category(product.getCategory().name())
                        .averageRating(product.getAverageRating())
                        .sold(product.getSold())
                        .stock(product.getStock())
                        .price(product.getPrice())
                        .imageUrl(product.getImageUrl())
                        .ratingCount(product.getRatingCount())
                        .discount(product.getDiscount())
                        .build();
    }

    private UserResponseDTO buildErrorResponse(RecommendationEngineError error) {
        return UserResponseDTO.builder()
                .error(error)
                .build();
    }

}
