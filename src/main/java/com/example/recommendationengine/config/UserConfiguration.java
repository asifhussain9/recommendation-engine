package com.example.recommendationengine.config;

import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.UserDTO;
import com.example.recommendationengine.dto.UserResponseDTO;
import com.example.recommendationengine.handler.UserHandler;
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

@Configuration
@Data
@AllArgsConstructor
public class UserConfiguration {
    private UserHandler userHandler;
    @Bean
    public RouterFunction<ServerResponse> routeUser() {
        return RouterFunctions.route()
                .POST("/user", this::createUser)
                .GET("/users", this::getAllUsers)
                .POST("/user/{id}", this::updateUser)
                .GET("/user/{id}", this::getUser)
                .DELETE("/user/{id}", this::deleteUser)
                .build();
    }

    private Mono<ServerResponse> createUser(ServerRequest request) {
        return userHandler.createUser(request.bodyToMono(UserDTO.class))
                .flatMap(user -> ServerResponse.ok().body(Mono.just(createUserResponseDTO(List.of(createUsereDTO(user)))), UserResponseDTO.class))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return userHandler.getAllUsers()
                .filter(this::isValidUser)
                .map(this::createUsereDTO)
                .collectList()
                .flatMap(userDTOS -> ServerResponse.ok().body(Flux.just(createUserResponseDTO(userDTOS)), UserResponseDTO.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> getUser(ServerRequest request) {
        return userHandler.getUser(request.pathVariable("id"))
                .filter(this::isValidUser)
                .map(this::createUsereDTO)
                .flatMap(userDTO -> ServerResponse.ok().body(Mono.just(createUserResponseDTO(List.of(userDTO))), UserResponseDTO.class))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> updateUser(ServerRequest request) {
        return userHandler.updateUser(request.pathVariable("id"), request.bodyToMono(UserDTO.class))
                .flatMap(user -> ServerResponse.ok().body(Mono.just(createUserResponseDTO(List.of(createUsereDTO(user)))), UserResponseDTO.class))
                .switchIfEmpty(ServerResponse.badRequest().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }

    private Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
        return userHandler.deleteUser(serverRequest.pathVariable("id"))
                .then(ServerResponse.noContent().build())
                .onErrorResume(throwable -> ServerResponse.ok().bodyValue(buildErrorResponse(RecommendationEngineError.builder().code("99").message(throwable.getMessage()).build())));
    }
    private UserResponseDTO createUserResponseDTO(List<UserDTO> userDTOs) {
        return UserResponseDTO.builder()
                .users(userDTOs)
                .build();
    }

    private UserDTO createUsereDTO(User user) {
        return UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .password("*********")
                        .build();
    }

    private UserResponseDTO buildErrorResponse(RecommendationEngineError error) {
        return UserResponseDTO.builder()
                .error(error)
                .build();
    }

    private boolean isValidUser(User user) {
        return SafeGetUtil.resolve(() -> user.getId()).isPresent() && user.getIsActive();
    }
}
