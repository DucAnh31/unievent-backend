package com.ducanh.unievent.security.service;

import com.ducanh.unievent.entity.RefreshToken;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.repository.RefreshTokenRepository;
import com.ducanh.unievent.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refresh-duration}")
    private Long refreshDuration;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    public RefreshToken createRefreshToken(String identifier)
    {
        User user = userRepository.findByUsernameOrEmail(identifier, identifier)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        return refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(UUID.randomUUID().toString())
                        .expiryDate(Instant.now().plusMillis(refreshDuration))
                        .revoked(false)
                        .user(user)
                        .build()
        );
    }

    public void verifyRefreshToken(RefreshToken refreshToken)
    {
        if(refreshToken.isRevoked())
            throw new ApiException(ErrorCode.REFRESH_TOKEN_REVOKED);
        if(refreshToken.getExpiryDate().isBefore(Instant.now()))
        {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
            throw new ApiException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }
    }

    public void revokeToken(String token)
    {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findRefreshToken(String token)
    {
        return refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }



}
