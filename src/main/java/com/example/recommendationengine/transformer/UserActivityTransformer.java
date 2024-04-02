package com.example.recommendationengine.transformer;

import com.example.recommendationengine.dto.request.UserActivitiesDTO;
import com.example.recommendationengine.handler.ProductHandler;
import com.example.recommendationengine.handler.UserHandler;
import com.example.recommendationengine.model.*;
import com.example.recommendationengine.util.SafeGetUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.ZoneOffset.UTC;

@Component
@RequiredArgsConstructor
public class UserActivityTransformer {
    private final UserHandler userHandler;
    private final ProductHandler productHandler;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UserActivityTransformer.class);
    private static final List<Activity> nonProductActivities = List.of(Activity.LOGIN, Activity.LOGOUT);
    public Mono<UserActivity> transform(UserActivitiesDTO userActivitiesDTO) {
        Mono<User> userMono = userHandler.getUser(userActivitiesDTO.getUserId());
        boolean isNonProductUserActivity = nonProductActivities.contains(Activity.valueOf(userActivitiesDTO.getActivity()));
        if (!validate(userActivitiesDTO, isNonProductUserActivity)) {
            return Mono.empty();
        }
        Mono<Product> productMono = Mono.empty();
        if (!isNonProductUserActivity) {
            productMono = productHandler.getProduct(userActivitiesDTO.getProductId());
        }
        if (SafeGetUtil.resolve(() -> Activity.valueOf(userActivitiesDTO.getActivity())).isEmpty()) {
            logger.error("Invalid activity: {}", userActivitiesDTO.getActivity());
            return Mono.empty();
        }
        return Mono.zip(userMono.defaultIfEmpty(new NullUser()), productMono.defaultIfEmpty(new NullProduct()))
                .flatMap(tuple -> {
                    User user = tuple.getT1();
                    Product product = tuple.getT2();
                    Activity activity = Activity.valueOf(userActivitiesDTO.getActivity());
                    UserActivity userActivity = UserActivity.builder()
                            .user(user)
                            .product(product)
                            .activity(activity)
                            .createdAt(LocalDateTime.now(UTC))
                            .updatedAt(LocalDateTime.now(UTC))
                            .build();
                    if (!validate(product, userActivitiesDTO, user, isNonProductUserActivity)) {
                        return Mono.empty();
                    }
                    return Mono.just(userActivity);
                })
                .onErrorResume(throwable -> {
                    logger.error("Error while saving user activity: {}, error {}", userActivitiesDTO.toString(), throwable.getMessage());
                    return Mono.empty();
                });
    }

    private static Boolean validate(UserActivitiesDTO userActivitiesDTO, boolean isNonProductUserActivity) {
        if (!isNonProductUserActivity && userActivitiesDTO.getProductId() == null) {
            logger.error("product id cannot be null");
            return false;
        }
        return true;
    }

    private Boolean validate(Product product, UserActivitiesDTO userActivitiesDTO, User user, boolean isNonProductUserActivity) {
        if (!isNonProductUserActivity && (product == null || product.isNullProduct())) {
            logger.error("Product not found: {}", userActivitiesDTO.getProductId());
            return false;
        }
        if (user == null) {
            logger.error("User not found: {}", userActivitiesDTO.getUserId());
            return false;
        }
        return true;
    }

    public UserActivitiesDTO toDTO(UserActivity userActivity) {
        return new UserActivitiesDTO(userActivity.getUser().getId(), userActivity.getActivity().name(), userActivity.getProduct().getId());
    }
}
