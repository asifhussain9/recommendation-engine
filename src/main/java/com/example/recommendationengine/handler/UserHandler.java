package com.example.recommendationengine.handler;

import com.example.recommendationengine.config.Properties;
import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.request.UserDTO;
import com.example.recommendationengine.model.User;
import com.example.recommendationengine.repository.UserRepository;
import com.example.recommendationengine.util.SafeGetUtil;
import com.mongodb.reactivestreams.client.MongoClients;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class UserHandler {
    private UserRepository userRepository;
    private Properties properties;
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<User> createUser(Mono<UserDTO> userDTOMono) {
        return userDTOMono.doOnNext(userDTO -> log.info("Creating user: {}", userDTO))
                .flatMap(userDTO -> {
                    LocalDateTime now = LocalDateTime.now();
                    String userId = StringUtils.isBlank(userDTO.getId()) ? UUID.randomUUID().toString() : userDTO.getId();
                    User user = User.builder()
                            .id(userId)
                            .name(userDTO.getName())
                            .email(userDTO.getEmail())
                            .password(userDTO.getPassword())
                            .lastLogin(now)
                            .createdAt(now)
                            .updatedAt(now)
                            .isActive(true)
                            .build();
                    return userRepository.save(user);
                }).doOnError(throwable -> {
                    log.error("Error creating user", throwable);
                });
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> updateUser(String id, Mono<UserDTO> userDTOMono) {
        Query findById = new Query(where("id").is(id));
        Update updateProperty = new Update();

        return userDTOMono.flatMap(userDTO -> {
            SafeGetUtil.resolve(() -> userDTO.getName()).ifPresent(name -> updateProperty.set("name", name));
            SafeGetUtil.resolve(() -> userDTO.getEmail()).ifPresent(email -> updateProperty.set("email", email));
            SafeGetUtil.resolve(() -> userDTO.getPassword()).ifPresent(password -> updateProperty.set("password", password));
            return reactiveMongoTemplate.findAndModify(findById, updateProperty, User.class);
        });
    }

    public Mono<User> getUser(String id) {
        return userRepository.findById(id)
                .doOnError(throwable -> {log.error("Error getting user", throwable);})
                .switchIfEmpty(Mono.error(RecommendationEngineError.builder().code("1").message("User not found").build()));
    }

    public Mono<Void> deleteUser(String id) {
        return userRepository.deleteById(id)
                .doOnError(throwable -> {log.error("Error deleting user", throwable);});
    }
}

