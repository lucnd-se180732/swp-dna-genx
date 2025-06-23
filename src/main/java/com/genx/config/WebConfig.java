package com.genx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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
