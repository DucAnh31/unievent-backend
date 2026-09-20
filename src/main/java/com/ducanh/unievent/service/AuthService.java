package com.ducanh.unievent.service;

import com.ducanh.unievent.common.enums.UserRole;
import com.ducanh.unievent.common.enums.UserStatus;
import com.ducanh.unievent.dto.request.LoginRequest;
import com.ducanh.unievent.dto.request.LogoutRequest;
import com.ducanh.unievent.dto.request.RefreshTokenRequest;
import com.ducanh.unievent.dto.request.RegisterRequest;
import com.ducanh.unievent.dto.response.AuthenticationResponse;
import com.ducanh.unievent.entity.RefreshToken;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.UserMapper;
import com.ducanh.unievent.repository.RefreshTokenRepository;
import com.ducanh.unievent.repository.UserRepository;
import com.ducanh.unievent.security.custom.CustomUserDetails;
import com.ducanh.unievent.security.custom.CustomUserDetailsService;
import com.ducanh.unievent.security.jwt.JwtService;
import com.ducanh.unievent.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CustomUserDetailsService userDetailsService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public AuthenticationResponse login(LoginRequest request)
    {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getIdentifier(),
                                request.getPassword()
                        )
                );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(request.getIdentifier());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request)
    {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.STUDENT);
        user.setStatus(UserStatus.ACTIVE);

        try {
            user = userRepository.saveAndFlush(user);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ApiException(ErrorCode.USER_EXISTED);
        }

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();


    }

    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenRequest request)
    {
        RefreshToken oldRefreshToken = refreshTokenService.findRefreshToken(request.getToken());

        refreshTokenService.verifyRefreshToken(oldRefreshToken);

        oldRefreshToken.setRevoked(true);
        User user = oldRefreshToken.getUser();

        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        String accessToken = jwtService.generateToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @Transactional
    public void logout(LogoutRequest request)
    {
        refreshTokenService.revokeToken(request.getRefreshToken());
    }
}
