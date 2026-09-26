package com.ducanh.unievent.controller;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.dto.request.LoginRequest;
import com.ducanh.unievent.dto.request.LogoutRequest;
import com.ducanh.unievent.dto.request.RefreshTokenRequest;
import com.ducanh.unievent.dto.request.RegisterRequest;
import com.ducanh.unievent.dto.response.AuthenticationResponse;
import com.ducanh.unievent.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@Valid @RequestBody RegisterRequest request)
    {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(authService.register(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authService.login(request)));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest request)
    {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(authService.refreshToken(request)));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody LogoutRequest request)
    {
        authService.logout(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }
}
