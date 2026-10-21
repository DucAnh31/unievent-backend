package com.ducanh.unievent.security.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenCookieService {
    public static final String COOKIE_NAME = "refreshToken";
    private static final String COOKIE_PATH = "/api/v1/auth";

    @Value("${app.auth.cookie.secure:true}")
    private boolean secure;

    @Value("${app.auth.cookie.same-site:Lax}")
    private String sameSite;

    @Value("${jwt.refresh-duration}")
    private long refreshDuration;

    public ResponseCookie create(String token) {
        return build(token, Duration.ofMillis(refreshDuration));
    }

    public ResponseCookie clear() {
        return build("", Duration.ZERO);
    }

    private ResponseCookie build(String value, Duration age) {
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(secure)
                .sameSite(sameSite)
                .path(COOKIE_PATH)
                .maxAge(age)
                .build();
    }
}
