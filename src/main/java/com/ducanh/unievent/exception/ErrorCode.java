package com.ducanh.unievent.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found"),
    USER_EXISTED(HttpStatus.BAD_REQUEST, "User existed"),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "Refresh Token not found"),
    REFRESH_TOKEN_REVOKED(HttpStatus.BAD_REQUEST, "Refresh Token revoked"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "Refresh Token expired"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden");



    private final HttpStatus status;
    private final String defaultMessage;
}
