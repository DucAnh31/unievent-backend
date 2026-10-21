package com.ducanh.unievent.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ducanh.unievent.exception.ApiException;
import com.ducanh.unievent.exception.ErrorCode;
import com.ducanh.unievent.security.service.RefreshTokenService;

@ExtendWith(MockitoExtension.class)
class AuthSessionTests {
    @Mock
    private RefreshTokenService refreshTokens;

    @InjectMocks
    private AuthService authService;

    @Test
    void missingOrBlankRefreshTokenIsRejectedBeforeLookup() {
        for (String token : new String[] {null, "", " "}) {
            ApiException error = assertThrows(ApiException.class, () -> authService.refreshToken(token));
            assertEquals(ErrorCode.REFRESH_TOKEN_NOT_FOUND, error.getErrorCode());
        }
        verifyNoInteractions(refreshTokens);
    }

    @Test
    void logoutWithoutTokenNeedsNoRedisLookup() {
        authService.logout(null);
        authService.logout("");
        authService.logout(" ");
        verifyNoInteractions(refreshTokens);
    }

    @Test
    void logoutRevokesExistingToken() {
        authService.logout("token");
        verify(refreshTokens).revokeToken("token");
    }

    @Test
    void logoutSucceedsWhenTokenNoLongerExists() {
        doThrow(new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND))
                .when(refreshTokens)
                .revokeToken("expired-token");
        assertDoesNotThrow(() -> authService.logout("expired-token"));
    }

    @Test
    void logoutDoesNotHideInfrastructureFailures() {
        RuntimeException failure = new RuntimeException("Redis unavailable");
        doThrow(failure).when(refreshTokens).revokeToken("token");
        assertSame(failure, assertThrows(RuntimeException.class, () -> authService.logout("token")));
    }
}
