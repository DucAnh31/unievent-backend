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
    USERNAME_EXISTED(HttpStatus.BAD_REQUEST, "Username existed"),
    EMAIL_EXISTED(HttpStatus.BAD_REQUEST, "Email existed"),

    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "Account is inactive or banned"),

    EVENT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Event category not found"),
    EVENT_CATEGORY_EXISTED(HttpStatus.BAD_REQUEST, "Event category existed"),
    EVENT_CATEGORY_IN_USE(HttpStatus.BAD_REQUEST, "Event category is in use"),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh Token not found"),
    REFRESH_TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "Refresh Token revoked"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh Token expired"),
    REFRESH_TOKEN_REUSE_DETECTED(HttpStatus.UNAUTHORIZED, "Refresh Token reuse detected"),

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Invalid data"),


    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),

    EVENT_CANNOT_BE_UPDATED(HttpStatus.BAD_REQUEST, "Event cannot be updated"),
    EVENT_CANNOT_BE_PUBLISH(HttpStatus.BAD_REQUEST, "Event cannot be publish"),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Event not found"),
    EVENT_CANNOT_BE_DELETED(HttpStatus.BAD_REQUEST, "Event cannot be deleted"),
    EVENT_CANNOT_CLOSE_REGISTRATION(HttpStatus.BAD_REQUEST, "Event cannot be closed registration"),
    EVENT_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "Event cannot be canceled"),
    INVALID_EVENT_TIME(HttpStatus.BAD_REQUEST, "Invalid event time"),



    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    private final HttpStatus status;
    private final String defaultMessage;
}
