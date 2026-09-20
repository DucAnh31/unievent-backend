package com.ducanh.unievent.repository;

import com.ducanh.unievent.entity.RefreshToken;
import com.ducanh.unievent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    public Optional<RefreshToken> findByToken(String token);
}
