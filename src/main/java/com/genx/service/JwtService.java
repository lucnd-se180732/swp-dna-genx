package com.genx.service;

    import com.genx.config.JwtConfig;
    import com.genx.entity.RefreshToken;
    import com.genx.entity.User;
    import com.genx.repository.IRefreshTokenRepository;
    import io.jsonwebtoken.Claims;
    import io.jsonwebtoken.Jwts;
    import io.jsonwebtoken.SignatureAlgorithm;
    import io.jsonwebtoken.security.Keys;
    import jakarta.transaction.Transactional;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import javax.crypto.SecretKey;
    import java.nio.charset.StandardCharsets;
    import java.time.Instant;
    import java.util.Date;
    import java.util.Optional;

@Service
    @Transactional(rollbackOn = Exception.class)
    public class JwtService {

        @Autowired
        private JwtConfig jwtConfig;

        @Autowired
        private IRefreshTokenRepository refreshTokenRepository;


        public String generateToken(String username, String role) {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtConfig.getExpiration());

            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));

            return Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        }

        public boolean validateToken(String token) {
            try {
                SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
                Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public String getEmailFromToken(String token) {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        }

        public String getRoleFromToken(String token) {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("role", String.class);
        }

        public String generateRefreshToken(String username, String role) {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtConfig.getRefreshExpiration()); // ví dụ: 30 ngày

            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));

            return Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();
        }

        public Claims parseToken(String token) {
            SecretKey key = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        public String getEmailFromRefreshToken(String token) {
            return parseToken(token).getSubject();
        }

        public boolean isRefreshTokenExpired(String token) {
            try {
                Date expiry = parseToken(token).getExpiration();
                return expiry.before(new Date());
            } catch (Exception e) {
                return true;
            }
        }

    public RefreshToken createRefreshToken(User user) {
        String tokenStr = generateRefreshToken(user.getUsername(), user.getRole().name());
        saveOrUpdateRefreshToken(user, tokenStr);
        return refreshTokenRepository.findByUser(user).orElseThrow(() ->
                new RuntimeException("Không tìm thấy refresh token sau khi tạo"));
    }

    public void saveOrUpdateRefreshToken(User user, String refreshTokenStr) {
        RefreshToken token = refreshTokenRepository.findByUser(user)
                .orElse(new RefreshToken());

        token.setUser(user);
        token.setRefreshToken(refreshTokenStr);
        token.setExpiryDate(Instant.now().plusSeconds(jwtConfig.getRefreshExpiration() / 1000));
        refreshTokenRepository.save(token);
    }

//        public RefreshToken createRefreshToken(User user) {
//            RefreshToken refreshToken = new RefreshToken();
//            refreshToken.setUser(user);
//            refreshToken.setRefreshToken(generateRefreshToken(user.getEmail(), user.getRole().name()));
//            refreshToken.setExpiryDate(Instant.now().plusSeconds(jwtConfig.getRefreshExpiration()/1000));
//            refreshTokenRepository.save(refreshToken);
//            return refreshToken;
//        }

//        public void saveOrUpdateRefreshToken(User user, String refreshTokenStr) {
//            Optional<RefreshToken> existingTokenOpt = refreshTokenRepository.findByUser(user);
//            if (existingTokenOpt.isPresent()) {
//                RefreshToken existingToken = existingTokenOpt.get();
//                existingToken.setRefreshToken(refreshTokenStr);
//                existingToken.setExpiryDate(Instant.now().plusSeconds(jwtConfig.getRefreshExpiration() / 1000));
//                refreshTokenRepository.save(existingToken);
//            } else {
//                RefreshToken newToken = new RefreshToken();
//                newToken.setUser(user);
//                newToken.setRefreshToken(refreshTokenStr);
//                newToken.setExpiryDate(Instant.now().plusSeconds(jwtConfig.getRefreshExpiration() / 1000));
//                refreshTokenRepository.save(newToken);
//            }
//        }

    }