package com.genx.controller;

import com.genx.dto.GoogleLoginRequest;
import com.genx.service.GoogleAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final GoogleAuthService googleAuthService;

    public AuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/google-login")
    public ResponseEntity<String> googleLogin(@RequestBody GoogleLoginRequest request) {
        String token = googleAuthService.loginWithGoogle(request.getCode());
        return ResponseEntity.ok(token);
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth API is working");
    }

    @GetMapping("/oauth2/callback/google")
    public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String code) {
        try {
            String token = googleAuthService.loginWithGoogle(code);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
}