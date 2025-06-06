package com.genx.service.impl;

import com.genx.config.GoogleAuthConfig;
import com.genx.config.JwtConfig;
import com.genx.dto.request.LoginRequest;
import com.genx.dto.request.UserCreationRequest;
import com.genx.dto.response.LoginResponse;
import com.genx.entity.RefreshToken;
import com.genx.entity.User;
import com.genx.enums.AuthProvider;
import com.genx.enums.ERole;
import com.genx.repository.IAuthRepository;
import com.genx.repository.IRefreshTokenRepository;
import com.genx.security.SecurityUtil;
import com.genx.service.JwtService;
import com.genx.service.interfaces.IAuthService;
import com.genx.service.interfaces.IUserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IAuthRepository userRepository;

    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GoogleAuthConfig googleAuthConfig;

    @Override
    public String registerUser(UserCreationRequest request) {
        // Kiểm tra username đã tồn tại
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Nếu có email thì kiểm tra luôn email (có thể cho phép không có email)
        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail()); // Có thể null
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(ERole.CUSTOMER);
        user.setAuthProvider(AuthProvider.SYSTEM);
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getInput())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Tạo access token
        String accessToken = jwtService.generateToken(user.getEmail(), user.getRole().name());

        // Tạo refresh token
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole().name());

        // Lưu refresh token vào DB (xóa token cũ nếu có)
        refreshTokenRepository.deleteByUser(user);
        RefreshToken newRefreshToken = new RefreshToken();
        newRefreshToken.setUser(user);
        newRefreshToken.setRefreshToken(refreshToken);
        newRefreshToken.setExpiryDate(Instant.now().plusSeconds(jwtConfig.getRefreshExpiration() / 1000));
        refreshTokenRepository.save(newRefreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }


    @Override
    public LoginResponse loginWithGoogle(String code) {
        try {
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    googleAuthConfig.getClientId(),
                    googleAuthConfig.getClientSecret(),
                    code,
                    googleAuthConfig.getRedirectUri()
            ).execute();

            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = new User();
                        newUser.setEmail(email);
                        newUser.setRole(ERole.CUSTOMER);
                        newUser.setUsername(email.split("@")[0]);
                        newUser.setPassword(null);
                        newUser.setAuthProvider(AuthProvider.GOOGLE);
                        newUser.setEnabled(true);
                        newUser.setAccountNonLocked(true);
                        return userRepository.save(newUser);
                    });

            if (!user.isEnabled()) throw new RuntimeException("User disabled");
            if (!user.isAccountNonLocked()) throw new RuntimeException("User locked");
            if (user.getAuthProvider() != AuthProvider.GOOGLE)
                throw new RuntimeException("Login bằng " + user.getAuthProvider());

            String accessToken = jwtService.generateToken(user.getEmail(), user.getRole().name());
            String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole().name());

            // Lưu refresh token (bạn cần tạo bảng `refresh_tokens` trước)
            jwtService.saveOrUpdateRefreshToken(user, refreshToken);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();
           // return new LoginResponse(accessToken, refreshToken, user.getEmail(), user.getRole().name());

        } catch (Exception e) {
          //  e.printStackTrace();
            throw new RuntimeException("Google login failed", e);
        }
    }

    @Override
    public LoginResponse refreshAccessToken(String refreshToken) {
        // Tìm refresh token trong DB
        RefreshToken tokenInDb = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ"));

        // Kiểm tra hạn sử dụng
        if (jwtService.isRefreshTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token đã hết hạn");
        }

        User user = tokenInDb.getUser();

        // Kiểm tra trạng thái tài khoản
        if (!user.isEnabled() || !user.isAccountNonLocked()) {
            throw new RuntimeException("Tài khoản bị vô hiệu hóa hoặc bị khóa");
        }

        // Tạo access token mới
        String newAccessToken = jwtService.generateToken(user.getEmail(), user.getRole().name());

        // Tạo refresh token mới (xoay vòng) và cập nhật DB
       String newRefreshTokenStr = jwtService.generateRefreshToken(user.getEmail(), user.getRole().name());
       jwtService.saveOrUpdateRefreshToken(user, newRefreshTokenStr);
        RefreshToken newToken = jwtService.createRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshTokenStr)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }


    @Override
    public void logout() {
        Optional<String> currentUsernameOpt = SecurityUtil.getCurrentUserLogin();

        if (currentUsernameOpt.isEmpty()) {
            throw new RuntimeException("User chưa đăng nhập");
        }

        String username = currentUsernameOpt.get();
        User user = userService.findByUsernameOrEmail(username);

        if (user == null) {
            throw new RuntimeException("User không tồn tại");
        }

        // Xóa refresh token của user khỏi DB
        refreshTokenRepository.deleteByUserId((user.getId()));
        }
    }



