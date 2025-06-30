package com.genx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3001")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

    }
//    @Value("${frontendUrl}")
//    private String frontendUrl;
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        String[] allowedOrigins = new String[] {
//                "http://localhost:3000",
//                frontendUrl
//        };
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*");
////        registry.addMapping("/**")
////                .allowedOrigins(allowedOrigins) // Cổng frontend
////                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
////                .allowedHeaders("*")
////                .exposedHeaders("Authorization")
////                .allowCredentials(true);
//
//    }
}
