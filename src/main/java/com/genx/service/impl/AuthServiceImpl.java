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
import com.genx.mapper.UserMapper;
import com.genx.repository.IAuthRepository;
import com.genx.repository.IRefreshTokenRepository;
import com.genx.security.CustomUserDetails;
import com.genx.service.JwtService;
import com.genx.service.interfaces.IAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Transactional(rollbackOn = Exception.class)
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IAuthRepository userRepository;

    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;

//    @Autowired
//    private IUserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtConfig jwtConfig;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GoogleAuthConfig googleAuthConfig;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String registerUser(UserCreationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }


        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return "User registered successfully";
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if (!user.isEnabled() || !user.isAccountNonLocked())
            throw new RuntimeException("Tài khoản bị vô hiệu hóa hoặc bị khóa");

        return buildLoginResponse(user);
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
            if (idToken == null) throw new RuntimeException("ID Token không hợp lệ");

            String email = idToken.getPayload().getEmail();

            User user = userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setUsername(email.split("@")[0]);
                newUser.setRole(ERole.CUSTOMER);
                newUser.setPassword(null);
                newUser.setAuthProvider(AuthProvider.GOOGLE);
                newUser.setEnabled(true);
                newUser.setAccountNonLocked(true);
                return userRepository.save(newUser);
            });

            if (!user.isEnabled() || !user.isAccountNonLocked())
                throw new RuntimeException("Tài khoản bị vô hiệu hóa hoặc khóa");

            if (user.getAuthProvider() != AuthProvider.GOOGLE)
                throw new RuntimeException("Vui lòng đăng nhập bằng " + user.getAuthProvider());

            return buildLoginResponse(user);

        } catch (Exception e) {
            throw new RuntimeException("Google login failed", e);
        }
    }

    private LoginResponse buildLoginResponse(User user) {
        String accessToken = jwtService.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getRole().name());
        jwtService.saveOrUpdateRefreshToken(user, refreshToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }



    @Override
    public LoginResponse refreshAccessToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token không hợp lệ"));

        if (jwtService.isRefreshTokenExpired(refreshToken))
            throw new RuntimeException("Refresh token đã hết hạn");

        User user = token.getUser();

        if (!user.isEnabled() || !user.isAccountNonLocked())
            throw new RuntimeException("Tài khoản bị vô hiệu hóa hoặc khóa");

        return buildLoginResponse(user);
//        String newAccessToken = jwtService.generateToken(user.getUsername(), user.getRole().name());
//        String newRefreshToken = jwtService.generateRefreshToken(user.getUsername(), user.getRole().name());
//
//        jwtService.saveOrUpdateRefreshToken(user, newRefreshToken);
//
//        return LoginResponse.builder()
//                .accessToken(newAccessToken)
//                .refreshToken(newRefreshToken)
//                .email(user.getEmail())
//                .role(user.getRole().name())
//                .build();
    }



    @Override
    public void logout(String refreshTokenFromCookie) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Long userId = userDetails.getUser().getId();
            refreshTokenRepository.deleteByUserId(userId);
            SecurityContextHolder.clearContext();
            return;
        }


        if (refreshTokenFromCookie != null && !refreshTokenFromCookie.isBlank()) {
            Claims claims = jwtService.parseToken(refreshTokenFromCookie);
            String email = claims.getSubject();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại"));

            refreshTokenRepository.deleteByUser(user);
            return;
        }

        // Không xác định được người dùng
        throw new RuntimeException("Không thể xác định người dùng để logout");
    }


}



