package com.example.recommendationengine.model;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
public class NullUser extends User {

    NullUser(String id, String name, String email, String password, LocalDateTime lastLogin, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isActive) {
        super(id, name, email, password, lastLogin, createdAt, updatedAt, isActive);
    }

    @Override
    public Boolean isNullUser() {
        return true;
    }
}
