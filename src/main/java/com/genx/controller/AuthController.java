package com.genx.controller;

import com.genx.dto.request.GoogleLoginRequest;
import com.genx.dto.request.LoginRequest;
import com.genx.dto.request.UserCreationRequest;
import com.genx.dto.response.ApiResponse;
import com.genx.dto.response.LoginResponse;
import com.genx.service.impl.AuthServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Value("${jwt.refresh-expiration}")
    private int refreshTokenExpiration;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCreationRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.login(request);

        // Gửi refreshToken vào cookie
        Cookie refreshCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true); // chỉ dùng HTTPS nếu bạn deploy thực tế
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(refreshTokenExpiration); // 7 ngày

        response.addCookie(refreshCookie);

        return ResponseEntity.ok(loginResponse);
    }


//    @GetMapping("/callback")
//    public ResponseEntity<LoginResponse> googleCallback(@RequestParam("code") String code) {
//        LoginResponse response = authService.loginWithGoogle(code);
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/login-google")
    public ResponseEntity<LoginResponse> googleLogin(@RequestBody GoogleLoginRequest request, HttpServletResponse response) {
        LoginResponse loginResponse = authService.loginWithGoogle(request.getCode());

        // Gửi refreshToken vào cookie
        Cookie refreshCookie = new Cookie("refreshToken", loginResponse.getRefreshToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(refreshTokenExpiration);

        response.addCookie(refreshCookie);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue("refreshToken") String refreshToken) {
        LoginResponse response = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }

   @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Boolean>> logout() {
        try {
            authService.logout();

            // Tạo cookie xóa refresh token trên client
            ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .secure(true)  // dev có thể chỉnh thành false nếu local
                    .path("/")
                    .maxAge(0)
                    .build();

            ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                    .code(1000)
                    .message("Đăng xuất thành công")
                    .result(null)
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(response);
        } catch (RuntimeException e) {
            ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                    .code(1000)
                    .message(e.getMessage())
                    .result(null)
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
    }




}