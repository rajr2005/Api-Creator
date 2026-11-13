package com.mockforge.mockforge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> allowedOrigins = Arrays.stream(
                        System.getenv().getOrDefault("CORS_ALLOWED_ORIGINS", "http://localhost:5173").split(","))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .toList();

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins.toArray(String[]::new))
                .allowedMethods("*")
                .allowedHeaders("*");
    }
}
