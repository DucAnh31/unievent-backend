package com.ducanh.unievent.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.exception.GlobalExceptionHandler;
import com.ducanh.unievent.security.service.RefreshTokenCookieService;
import com.ducanh.unievent.service.AuthService;
import com.ducanh.unievent.service.model.AuthTokenResult;

@ExtendWith(MockitoExtension.class)
class AuthCookieTests {
    @Mock
    private AuthService authService;

    private RefreshTokenCookieService cookies;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        cookies = new RefreshTokenCookieService();
        ReflectionTestUtils.setField(cookies, "secure", true);
        ReflectionTestUtils.setField(cookies, "sameSite", "Lax");
        ReflectionTestUtils.setField(cookies, "refreshDuration", 604800000L);
        mvc = MockMvcBuilders.standaloneSetup(new AuthController(authService, cookies))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void loginKeepsRefreshTokenOutOfJsonAndSetsProtectedCookie() throws Exception {
        when(authService.login(any())).thenReturn(new AuthTokenResult("access-token", "refresh-secret"));

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
								{"identifier":"student","password":"password"}
								"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("access-token"))
                .andExpect(jsonPath("$.data.refreshToken").doesNotExist())
                .andExpect(content().string(not(containsString("refresh-secret"))))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken=refresh-secret")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("HttpOnly")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Secure")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("SameSite=Lax")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/api/v1/auth")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=604800")));
    }

    @Test
    void refreshReadsCookieWithoutBodyAndReplacesIt() throws Exception {
        when(authService.refreshToken("old-token")).thenReturn(new AuthTokenResult("new-access", "new-refresh"));

        mvc.perform(post("/api/v1/auth/refresh").cookie(new Cookie("refreshToken", "old-token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").value("new-access"))
                .andExpect(jsonPath("$.data.refreshToken").doesNotExist())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken=new-refresh")));

        verify(authService).refreshToken("old-token");
    }

    @Test
    void missingRefreshCookieReturnsUnauthorized() throws Exception {
        when(authService.refreshToken(null)).thenThrow(new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        mvc.perform(post("/api/v1/auth/refresh"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist(HttpHeaders.SET_COOKIE));
    }

    @Test
    void logoutRevokesCookieTokenAndClearsSameCookiePath() throws Exception {
        mvc.perform(post("/api/v1/auth/logout").cookie(new Cookie("refreshToken", "current-token")))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken=;")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Path=/api/v1/auth")));

        verify(authService).logout("current-token");
    }

    @Test
    void logoutWithoutCookieStillClearsCookie() throws Exception {
        mvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("Max-Age=0")));

        verify(authService).logout(null);
    }

    @Test
    void localHttpConfigurationOmitsSecureFlag() {
        ReflectionTestUtils.setField(cookies, "secure", false);

        org.junit.jupiter.api.Assertions.assertFalse(cookies.create("token").isSecure());
        org.junit.jupiter.api.Assertions.assertTrue(cookies.create("token").isHttpOnly());
    }
}
