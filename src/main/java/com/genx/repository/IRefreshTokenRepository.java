package com.genx.repository;

import com.genx.entity.RefreshToken;
import com.genx.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
   // Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken>  findByUserId(Long userId);
    void deleteByUser(User user);
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByRefreshToken(String refreshToken);


    void deleteByUserId(Long userId);
}
