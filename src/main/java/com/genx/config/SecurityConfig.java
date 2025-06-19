package com.genx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/registrations/register").permitAll() // Cho phép mà không cần login
                        .anyRequest().permitAll() // ✅ Cho tất cả endpoint khác được phép luôn (nếu đang test)
                );

        return http.build();
    }
}
