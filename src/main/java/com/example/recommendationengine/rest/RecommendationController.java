package com.example.recommendationengine.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RecommendationController {
    @PostMapping("/recommend")
    public String recommend() {
        return "recommendation";
    }

}
