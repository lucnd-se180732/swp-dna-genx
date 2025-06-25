package com.genx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
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
