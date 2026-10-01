package com.ducanh.unievent.service;

import com.ducanh.unievent.common.enums.UserRole;
import com.ducanh.unievent.common.enums.UserStatus;
import com.ducanh.unievent.common.service.RedisService;
import com.ducanh.unievent.common.util.Sha256Util;
import com.ducanh.unievent.dto.request.*;
import com.ducanh.unievent.dto.response.AuthenticationResponse;
import com.ducanh.unievent.dto.response.VerifyPasswordResponse;
import com.ducanh.unievent.entity.User;
import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.mapper.UserMapper;
import com.ducanh.unievent.repository.UserRepository;
import com.ducanh.unievent.security.custom.CustomUserDetails;
import com.ducanh.unievent.security.custom.CustomUserDetailsService;
import com.ducanh.unievent.security.jwt.JwtService;
import com.ducanh.unievent.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private final SecureRandom secureRandom = new SecureRandom();
    private final RedisService redisService;
    private final EmailService emailService;

    private static final String OTP_KEY_PREFIX = "auth:password:otp:";
    private static final String OTP_COOLDOWN_KEY_PREFIX = "auth:password:otp:cooldown:";
    private static final String OTP_SEND_COUNT_KEY_PREFIX = "auth:password:otp:send-count:";
    private static final String OTP_ATTEMPTS_KEY_PREFIX = "auth:password:otp:attempts:";
    private static final String RESET_TOKEN_KEY_PREFIX = "auth:password:token:";

    private static final String EMAIL_VERIFY_TOKEN_KEY_PREFIX = "auth:email:verify:token:";
    private static final String EMAIL_VERIFY_COOLDOWN_KEY_PREFIX = "auth:email:verify:cooldown:";
    private static final String EMAIL_VERIFY_SEND_COUNT_KEY_PREFIX = "auth:email:verify:send-count:";

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

        if (!userDetails.isEmailVerified()) {
            throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED);
        }

        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void register(RegisterRequest request)
    {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.STUDENT);
        user.setStatus(UserStatus.ACTIVE);
        user.setEmailVerifiedAt(null);

        try {
            user = userRepository.saveAndFlush(user);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new ApiException(ErrorCode.USER_EXISTED);
        }

        // co the bo sung affter commit de rach roi phan @transactional va gui mail
        String token = createEmailVerificationToken(user.getId());
        emailService.sendVerificationEmail(user.getEmail(), token);


        // phai xac thuc email truoc da, duoi day la code cu khi khong co luong verify email
//        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
//
//        String accessToken = jwtService.generateToken(userDetails);
//        String refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
//
//        return AuthenticationResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
//                .build();
    }



    private String createEmailVerificationToken(Long userId)
    {
        String token = UUID.randomUUID().toString();

        String key = EMAIL_VERIFY_TOKEN_KEY_PREFIX + Sha256Util.sha256(token);

        redisService.setWithTTL(key, userId.toString(), 24, TimeUnit.HOURS);

        return token;
    }

    @Transactional
    public void verifyEmail(VerifyEmailRequest request)
    {

        String token = request.getToken();
        String storedKey = EMAIL_VERIFY_TOKEN_KEY_PREFIX + Sha256Util.sha256(token);

        String userIdValue = redisService.getAndDelete(storedKey);
        if(userIdValue == null)
            throw new ApiException(ErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN);
        Long userId = Long.parseLong(userIdValue);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        user.setEmailVerifiedAt(Instant.now());
        userRepository.save(user);

    }

    public void resendVerificationEmail(ResendVerificationEmailRequest request)
    {
        String key = Sha256Util.sha256(request.getIdentifier());
        String cooldownKey = EMAIL_VERIFY_COOLDOWN_KEY_PREFIX + key;

        boolean canSend = redisService.setIfAbsentWithTTL(cooldownKey, "1", Duration.ofSeconds(60));
        if(!canSend)
            throw new ApiException(ErrorCode.TOO_MANY_VERIFICATION_EMAIL_REQUESTS);

        String sendCoundKey = EMAIL_VERIFY_SEND_COUNT_KEY_PREFIX + key;
        redisService.setIfAbsentWithTTL(sendCoundKey, "0", Duration.ofHours(1));
        if(redisService.increment(sendCoundKey) > 5)
            throw new ApiException(ErrorCode.TOO_MANY_VERIFICATION_EMAIL_REQUESTS);

        User user = userRepository.findByUsernameOrEmail(request.getIdentifier(), request.getIdentifier()).orElse(null);

        if(user == null || user.getEmailVerifiedAt() != null)
            return;

        String token = createEmailVerificationToken(user.getId());
        emailService.sendVerificationEmail(user.getEmail(), token);
    }




    public AuthenticationResponse refreshToken(RefreshTokenRequest request)
    {
        Long userId = refreshTokenService.verifyAndGetUserId(request.getToken());

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        CustomUserDetails userDetails = CustomUserDetails.build(user);
        if(!userDetails.isEnabled())
            throw new ApiException(ErrorCode.ACCOUNT_DISABLED);

        if(!userDetails.isEmailVerified())
            throw new ApiException(ErrorCode.EMAIL_NOT_VERIFIED);

        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = refreshTokenService.rotateRefreshToken(request.getToken(), userId);

        return AuthenticationResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    public void logout(LogoutRequest request)
    {
        refreshTokenService.revokeToken(request.getRefreshToken());
    }


    // forgot password



    private String generateOtp()
    {
        int optNumber = secureRandom.nextInt(1000000);
        return String.format("%06d", optNumber);
    }

    private void saveOtp(String emailKey, String otp)
    {
        String key = OTP_KEY_PREFIX + emailKey;
        String value = passwordEncoder.encode(otp);
        redisService.setWithTTL(key, value, 5, TimeUnit.MINUTES);
    }
//    private long incrementOtpAttempts(String key) {
//        redisService.setIfAbsentWithTTL(key, "0", Duration.ofMinutes(15));
//
//        Long count = redisService.increment(key);
//
//        return count;
//    }


    public VerifyPasswordResponse verifyPasswordOtp(VerifyPasswordRequest request)
    {
        String email = request.getEmail();
        String emailKey = Sha256Util.sha256(email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_OTP));

        String key = OTP_KEY_PREFIX + emailKey;
        String attemptsKey = OTP_ATTEMPTS_KEY_PREFIX + emailKey;

        String storedOtp = redisService.get(key);

        if (storedOtp == null) {
            throw new ApiException(ErrorCode.INVALID_OTP);
        }

        redisService.setIfAbsentWithTTL(attemptsKey, "0", Duration.ofMinutes(15));
        if(redisService.increment(key) > 5)
            throw new ApiException(ErrorCode.TOO_MANY_OTP_ATTEMPTS);

//        long attempts = incrementOtpAttempts(attemptsKey);
//
//        if(attempts > 5)
//            throw new ApiException(ErrorCode.TOO_MANY_OTP_ATTEMPTS);

        if(!passwordEncoder.matches(request.getOtp(), storedOtp))
            throw new ApiException(ErrorCode.INVALID_OTP);

        String consumedHash = redisService.getAndDelete(key);
        if (!storedOtp.equals(consumedHash)) {
            throw new ApiException(ErrorCode.INVALID_OTP);
        }

        String resetToken = UUID.randomUUID().toString();

        String resetTokenKey = RESET_TOKEN_KEY_PREFIX + Sha256Util.sha256(resetToken);

        redisService.setWithTTL(resetTokenKey, user.getId().toString(), 10, TimeUnit.MINUTES);

        return VerifyPasswordResponse.builder()
                .resetToken(resetToken)
                .build();

    }

    public void forgotPassword(ForgotPasswordRequest request)
    {
        String email = request.getEmail();
        String emailKey = Sha256Util.sha256(email);

        if (!userRepository.existsByEmail(email)) {
            return;
        }

        String cooldownKey = OTP_COOLDOWN_KEY_PREFIX + emailKey;
        String sendCountKey = OTP_SEND_COUNT_KEY_PREFIX + emailKey;

        boolean canSend = redisService.setIfAbsentWithTTL(
                cooldownKey, "1", Duration.ofSeconds(60)
        );

        if (!canSend)
            throw new ApiException(ErrorCode.TOO_MANY_OTP_REQUESTS);
        redisService.setIfAbsentWithTTL(sendCountKey, "0", Duration.ofHours(1));

        if (redisService.increment(sendCountKey) > 5)
            throw new ApiException(ErrorCode.TOO_MANY_OTP_REQUESTS);

        String otp = generateOtp();
        saveOtp(emailKey, otp);

        emailService.sendOtp(email, otp);

    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request)
    {
        String tokenKey = RESET_TOKEN_KEY_PREFIX + Sha256Util.sha256(request.getResetToken());

        String userIdValue = redisService.getAndDelete(tokenKey);
        if (userIdValue == null) {
            throw new ApiException(ErrorCode.INVALID_RESET_TOKEN);
        }

        User user = userRepository.findById(Long.valueOf(userIdValue))
                .orElseThrow(() -> new ApiException(ErrorCode.INVALID_RESET_TOKEN));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}
