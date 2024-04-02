package com.example.recommendationengine.component;

import com.example.recommendationengine.model.RecommendationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendationEngine {
    private final RecommendationFactory similarProductsRecommendationFactory;
    private final RecommendationFactory sellingOutProductsRecommendationFactory;
    private final RecommendationFactory recentlyViewedProductsRecommendationFactory;
    private final RecommendationFactory highRatedProductsRecommendationFactory;
    private final RecommendationFactory popularProductsRecommendationFactory;
    private final RecommendationFactory discountedProductsRecommendationFactory;
    private final RecommendationFactory alsoBoughtProductsRecommendationFactory;

    public RecommendationFactory getRecommendationFactory(RecommendationType recommendationType) {
        return switch (recommendationType) {
            case SIMILAR_PRODUCTS -> similarProductsRecommendationFactory;
            case SELLING_OUT_PRODUCTS -> sellingOutProductsRecommendationFactory;
            case RECENTLY_VIEWED_PRODUCTS -> recentlyViewedProductsRecommendationFactory;
            case HIGH_RATED_PRODUCTS -> highRatedProductsRecommendationFactory;
            case POPULAR_PRODUCTS -> popularProductsRecommendationFactory;
            case DISCOUNTED_PRODUCTS -> discountedProductsRecommendationFactory;
            case ALSO_BOUGHT_PRODUCTS -> alsoBoughtProductsRecommendationFactory;
            default -> null;
        };
    }
}
