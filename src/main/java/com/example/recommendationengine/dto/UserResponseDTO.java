package com.example.recommendationengine.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserResponseDTO {
    RecommendationEngineError error;
    List<UserDTO> users;
}
