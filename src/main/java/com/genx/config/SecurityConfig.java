package com.genx.config;

import com.genx.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAnyRole;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${frontendUrl}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/adn-results/lookup",
                                "/api/v1/auth/**",
                                "/api/blogs/**",
                                "/payment-result",
                                "/api/vnpay/vnpay-return",
                                "/api/adn-results/export/{bookingId}",
                                "/api/blogs",
                                "/api/blogs/all",
                                "/api/blogs/{id:[\\d]+}",
                                "/api/services/**"
                        ).permitAll()


                        .requestMatchers("/ws/**").permitAll()

                        .requestMatchers("/topic/**", "/queue/**", "/user/**").authenticated()


                        .requestMatchers("/api/v1/rooms/**", "/app/**")
                        .hasAnyRole("CUSTOMER", "RECORDER_STAFF")

                        .requestMatchers("/api/v1/auth/logout").authenticated()
                        .requestMatchers("/api/notifications/**")
                        .access(hasAnyRole("CUSTOMER", "RECORDER_STAFF", "LAB_STAFF", "ADMIN"))

                        .requestMatchers("/api/v1/staff/**", "/api/staff/dashboard").hasRole("RECORDER_STAFF")
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/adn-results/**").hasRole("LAB_STAFF")
                        .requestMatchers(
                                "/api/registrations/**",
                                "/api/v1/customer/**",
                                "/api/vnpay/**"
                        ).hasRole("CUSTOMER")


                        .requestMatchers(
                                "/api/blogs",
                                "/api/blogs/all",
                                "/api/blogs/{id:[\\d]+}"
                        ).permitAll()
                        .requestMatchers("/api/registrations/**", "/api/v1/customer/sample-collection/**", "/api/vnpay/**").hasRole("CUSTOMER")

                        .requestMatchers(
                                "/api/blogs",
                                "/api/blogs/{id:[\\d]+}"
                        ).hasRole("ADMIN")

                        .requestMatchers("/api/blogs/rate").hasRole("CUSTOMER")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/dashboard/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", frontendUrl));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
