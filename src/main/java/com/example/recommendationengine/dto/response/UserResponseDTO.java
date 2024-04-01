package com.example.recommendationengine.dto.response;

import com.example.recommendationengine.dto.RecommendationEngineError;
import com.example.recommendationengine.dto.request.UserDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserResponseDTO {
    RecommendationEngineError error;
    List<UserDTO> users;
}
