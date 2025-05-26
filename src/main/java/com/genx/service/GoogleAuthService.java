package com.genx.service;

import com.genx.config.GoogleAuthConfig;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Service;

@Service
public class GoogleAuthService {
    private final GoogleAuthConfig googleAuthConfig;
    private final JwtService jwtService;

    public GoogleAuthService(GoogleAuthConfig googleAuthConfig, JwtService jwtService) {
        this.googleAuthConfig = googleAuthConfig;
        this.jwtService = jwtService;
    }

    public String loginWithGoogle(String code) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                new GsonFactory(),
                googleAuthConfig.getClientId(),
                googleAuthConfig.getClientSecret(),
                code,
                googleAuthConfig.getRedirectUri()
            ).execute();

            String email = tokenResponse.parseIdToken().getPayload().getEmail();
            // Here you would typically:
            // 1. Check if user exists in your database
            // 2. Create user if they don't exist
            // 3. Generate JWT token

            return jwtService.generateToken(email);
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Google login", e);
        }
    }
}