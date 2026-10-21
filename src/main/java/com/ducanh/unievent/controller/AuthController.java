package com.ducanh.unievent.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.*;
import com.ducanh.unievent.dto.response.AuthenticationResponse;
import com.ducanh.unievent.dto.response.VerifyPasswordResponse;
import com.ducanh.unievent.security.service.RefreshTokenCookieService;
import com.ducanh.unievent.service.AuthService;
import com.ducanh.unievent.service.model.AuthTokenResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final RefreshTokenCookieService refreshTokenCookieService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginRequest request) {

        return authenticationResponse(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @CookieValue(name = RefreshTokenCookieService.COOKIE_NAME, required = false) String refreshToken) {
        return authenticationResponse(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = RefreshTokenCookieService.COOKIE_NAME, required = false) String refreshToken) {

        authService.logout(refreshToken);

        return ResponseEntity.status(HttpStatus.OK)
                .header(
                        HttpHeaders.SET_COOKIE,
                        refreshTokenCookieService.clear().toString())
                .body(ApiResponse.success(null));
    }

    private ResponseEntity<ApiResponse<AuthenticationResponse>> authenticationResponse(AuthTokenResult tokens) {
        AuthenticationResponse response = AuthenticationResponse.builder()
                .accessToken(tokens.getAccessToken())
                .build();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.SET_COOKIE,
                        refreshTokenCookieService
                                .create(tokens.getRefreshToken())
                                .toString())
                .body(ApiResponse.success(response));
    }

    // forgot password

    @PostMapping("/password/forgot")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

    @PostMapping("/password/otp/verify")
    public ResponseEntity<ApiResponse<VerifyPasswordResponse>> verifyPasswordOtp(
            @Valid @RequestBody VerifyPasswordRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(authService.verifyPasswordOtp(request)));
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null, "Password updated successfully"));
    }

    @PostMapping("/email/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        authService.verifyEmail(request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }

    @PostMapping("/email/verification-resend")
    public ResponseEntity<ApiResponse<Void>> resendVerificationEmail(
            @Valid @RequestBody ResendVerificationEmailRequest request) {
        authService.resendVerificationEmail(request);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }
}
