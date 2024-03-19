package com.example.recommendationengine.handler;

import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.UserDTO;
import com.example.recommendationengine.model.User;
import com.example.recommendationengine.repository.UserRepository;
import com.example.recommendationengine.util.SafeGetUtil;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class UserHandler {
    private UserRepository userRepository;

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
        return userRepository.findById(id)
                .flatMap(user -> userDTOMono.doOnNext(userDTO -> log.info("Updating user: {}", userDTO))
                        .flatMap(userDTO -> {
                            SafeGetUtil.resolve(() -> userDTO.getName()).ifPresent(user::setName);
                            SafeGetUtil.resolve(() -> userDTO.getEmail()).ifPresent(user::setEmail);
                            SafeGetUtil.resolve(() -> userDTO.getPassword()).ifPresent(user::setPassword);
                            user.setUpdatedAt(LocalDateTime.now());
                            user.setIsActive(true);
                            return userRepository.save(user);
                        })).doOnError(throwable -> {log.error("Error updating user", throwable);})
                .switchIfEmpty(Mono.error(RecommendationEngineError.builder().code("1").message("User not found").build()));
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

