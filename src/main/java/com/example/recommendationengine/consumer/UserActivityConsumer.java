package com.example.recommendationengine.consumer;

import com.example.recommendationengine.dto.UserActivitiesDTO;
import com.example.recommendationengine.handler.ProductHandler;
import com.example.recommendationengine.handler.UserHandler;
import com.example.recommendationengine.model.*;
import com.example.recommendationengine.repository.UserActivityRepository;
import com.example.recommendationengine.util.SafeGetUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserActivityConsumer {
    private final UserHandler userHandler;
    private final ProductHandler productHandler;
    private final UserActivityRepository userActivityRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(UserActivityConsumer.class);
    private static final List<Activity> nonProductActivities = List.of(Activity.LOGIN, Activity.LOGOUT);

    @KafkaListener(topics = "user.activities", groupId = "recommendation-engine")
    public void consume(String message) {
        logger.info("Consuming message: " + message);
        try {
            UserActivitiesDTO userActivitiesDTO = objectMapper.readValue(message, UserActivitiesDTO.class);
            Mono<User> userMono = userHandler.getUser(userActivitiesDTO.getUserId());
            boolean isNonProductUserActivity = nonProductActivities.contains(Activity.valueOf(userActivitiesDTO.getActivity()));
            if(!validate(userActivitiesDTO, isNonProductUserActivity)) {
                return;
            }
            Mono<Product> productMono = Mono.just(new NullProduct());
            if(!isNonProductUserActivity) {
                productMono = productHandler.getProduct(userActivitiesDTO.getProductId());
            }
            if(SafeGetUtil.resolve(() -> Activity.valueOf(userActivitiesDTO.getActivity())).isEmpty()) {
                logger.error("Invalid activity: {}", userActivitiesDTO.getActivity());
                return;
            }
            Mono.zip(userMono, productMono)
                    .flatMap(tuple -> {
                        User user = tuple.getT1();
                        Product product = tuple.getT2();
                        Activity activity = Activity.valueOf(userActivitiesDTO.getActivity());
                        UserActivity userActivity = UserActivity.builder().user(user).product(product).activity(activity).build();
                        if(!validate(activity, product, userActivitiesDTO, user, isNonProductUserActivity)) {
                            return Mono.empty();
                        }
                        return userActivityRepository.save(userActivity);
                    })
                    .switchIfEmpty(Mono.error(new RuntimeException("Invalid User or Product")))
                    .doOnError(throwable -> logger.error("Error while saving user activity: {}, error {}", userActivitiesDTO.toString(), throwable.getMessage()))
                    .doFinally(tuple -> logger.info("Consumed message: {}", userActivitiesDTO))
                    .subscribe();
        } catch (JsonProcessingException e) {
            logger.error("Error while processing message: {}", e.getMessage());
        }
    }

    private Boolean validate(UserActivitiesDTO userActivitiesDTO, boolean isNonProductUserActivity) {
        if(!isNonProductUserActivity && userActivitiesDTO.getProductId() == null) {
            logger.error("product id cannot be null");
            return false;
        }
        return true;
    }

    private Boolean validate(Activity activity, Product product, UserActivitiesDTO userActivitiesDTO, User user, boolean isNonProductUserActivity) {
        if(!isNonProductUserActivity && (product == null || product.isNullProduct())) {
            logger.error("Product not found: {}", userActivitiesDTO.getProductId());
            return false;
        }
        if(user == null) {
            logger.error("User not found: {}", userActivitiesDTO.getUserId());
            return false;
        }
        return true;
    }
}
