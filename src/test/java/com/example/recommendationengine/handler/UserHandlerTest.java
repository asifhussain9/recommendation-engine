package com.example.recommendationengine.handler;

import com.example.recommendationengine.config.Properties;
import com.example.recommendationengine.dto.request.UserDTO;
import com.example.recommendationengine.model.User;
import com.example.recommendationengine.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserHandlerTest {
    private UserRepository userRepository;
    private UserHandler userHandler;
    private Properties properties;
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @BeforeEach
    public void setup() {
        userRepository = Mockito.mock(UserRepository.class);
        userHandler = new UserHandler(userRepository, properties, reactiveMongoTemplate);
    }

    @Test
    public void testCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Test User");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        User user = User.builder()
                .id(UUID.randomUUID().toString())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .lastLogin(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isActive(true)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userHandler.createUser(Mono.just(userDTO)))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    public void testGetAllUsers() {
        User user1 = User.builder().id("1").build();
        User user2 = User.builder().id("2").build();

        when(userRepository.findAll()).thenReturn(Flux.just(user1, user2));

        StepVerifier.create(userHandler.getAllUsers())
                .expectNext(user1, user2)
                .verifyComplete();
    }


}