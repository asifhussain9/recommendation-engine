package com.example.recommendationengine.handler;

import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.request.ProductDTO;
import com.example.recommendationengine.model.Product;
import com.example.recommendationengine.model.ProductCategory;
import com.example.recommendationengine.repository.ProductRepository;
import com.example.recommendationengine.util.SafeGetUtil;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class ProductHandler {
    private ProductRepository productRepository;

    public Mono<Product> createProduct(Mono<ProductDTO> productDTOMono) {
        return productDTOMono.doOnNext(productDTO -> log.info("Creating product: {}", productDTO))
                .flatMap(productDTO -> {
                    String productId = StringUtils.isBlank(productDTO.getId()) ? UUID.randomUUID().toString() : productDTO.getId();
                    Product product = Product.builder()
                            .id(productId)
                            .name(productDTO.getName())
                            .description(productDTO.getDescription())
                            .category(ProductCategory.valueOf(productDTO.getCategory()))
                            .subCategory(productDTO.getSubCategory())
                            .imageUrl(productDTO.getImageUrl())
                            .price(productDTO.getPrice())
                            .averageRating(productDTO.getAverageRating())
                            .stock(productDTO.getStock())
                            .discount(productDTO.getDiscount())
                            .ratingCount(productDTO.getRatingCount())
                            .sold(productDTO.getSold())
                            .build();
                    return productRepository.save(product);
                }).doOnError(throwable -> {
                    log.error("Error creating product", throwable);
                });
    }

    public Flux<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Mono<Product> updateProduct(String id, Mono<ProductDTO> productDTOMono) {
        return productRepository.findById(id)
                .flatMap(product -> productDTOMono.doOnNext(productDTO -> log.info("Updating product: {}", productDTO))
                        .flatMap(productDTO -> {
                            SafeGetUtil.resolve(() -> productDTO.getName()).ifPresent(product::setName);
                            SafeGetUtil.resolve(() -> productDTO.getDescription()).ifPresent(product::setDescription);
                            SafeGetUtil.resolve(() -> ProductCategory.valueOf(productDTO.getCategory())).ifPresent(product::setCategory);
                            SafeGetUtil.resolve(() -> productDTO.getDiscount()).ifPresent(product::setDiscount);
                            SafeGetUtil.resolve(() -> productDTO.getSold()).ifPresent(product::setSold);
                            SafeGetUtil.resolve(() -> productDTO.getImageUrl()).ifPresent(product::setImageUrl);
                            SafeGetUtil.resolve(() -> productDTO.getAverageRating()).ifPresent(product::setAverageRating);
                            SafeGetUtil.resolve(() -> productDTO.getStock()).ifPresent(product::setStock);
                            SafeGetUtil.resolve(() -> productDTO.getPrice()).ifPresent(product::setPrice);
                            SafeGetUtil.resolve(() -> productDTO.getRatingCount()).ifPresent(product::setRatingCount);
                            return productRepository.save(product);
                        })).doOnError(throwable -> {log.error("Error updating product", throwable);})
                .switchIfEmpty(Mono.error(RecommendationEngineError.builder().code("1").message("Product not found").build()));
    }

    public Mono<Product> getProduct(String id) {
        return productRepository.findById(id)
                .doOnError(throwable -> {log.error("Error getting product", throwable);})
                .switchIfEmpty(Mono.error(RecommendationEngineError.builder().code("1").message("Product not found").build()));
    }

    public Mono<Void> deleteProduct(String id) {
        return productRepository.deleteById(id)
                .doOnError(throwable -> {log.error("Error deleting product", throwable);});
    }

    public Flux<Product> getDiscountedProducts(Double minDiscount) {
        minDiscount = Optional.ofNullable(minDiscount).orElse(0.0);
        Sort sortByDiscountDesc = Sort.by(Sort.Direction.DESC, "discount");
        Pageable topTen = PageRequest.of(0, 10, sortByDiscountDesc);
        return productRepository.findByDiscountGreaterThan(0.0, topTen);
    }
}

