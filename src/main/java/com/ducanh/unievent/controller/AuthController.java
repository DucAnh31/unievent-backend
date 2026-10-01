package com.ducanh.unievent.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.*;
import com.ducanh.unievent.dto.response.AuthenticationResponse;
import com.ducanh.unievent.dto.response.VerifyPasswordResponse;
import com.ducanh.unievent.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(authService.refreshToken(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request) {
        authService.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
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
