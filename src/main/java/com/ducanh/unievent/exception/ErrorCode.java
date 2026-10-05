package com.ducanh.unievent.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized error"),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_EXISTED(HttpStatus.CONFLICT, "User already existed"),
    USERNAME_EXISTED(HttpStatus.CONFLICT, "Username already existed"),
    EMAIL_EXISTED(HttpStatus.CONFLICT, "Email already existed"),

    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, "Account is inactive or banned"),

    EVENT_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "Event category not found"),
    EVENT_CATEGORY_EXISTED(HttpStatus.CONFLICT, "Event category already existed"),
    EVENT_CATEGORY_IN_USE(HttpStatus.CONFLICT, "Event category is in use"),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh Token not found"),
    REFRESH_TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "Refresh Token has been revoked"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh Token has expired"),
    REFRESH_TOKEN_REUSE_DETECTED(HttpStatus.UNAUTHORIZED, "Refresh Token reuse detected"),

    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Invalid data"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),

    EVENT_CANNOT_BE_UPDATED(HttpStatus.CONFLICT, "Only draft events can be updated"),
    EVENT_CANNOT_BE_PUBLISH(HttpStatus.CONFLICT, "Event cannot be publish"),
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Event not found"),
    EVENT_CANNOT_BE_DELETED(HttpStatus.CONFLICT, "Only draft events can be deleted"),
    EVENT_CANNOT_CLOSE_REGISTRATION(HttpStatus.CONFLICT, "Registration cannot be closed for this event"),
    EVENT_CANNOT_CANCEL(HttpStatus.CONFLICT, "Event cannot be canceled"),
    EVENT_CANNOT_BE_SUBMITTED(HttpStatus.CONFLICT, "Event cannot be submitted for approval"),
    EVENT_CANNOT_BE_APPROVED(HttpStatus.CONFLICT, "Event cannot be approved"),
    EVENT_CANNOT_BE_REJECTED(HttpStatus.CONFLICT, "Only events pending approval can be rejected"),
    INVALID_EVENT_TIME(HttpStatus.BAD_REQUEST, "Invalid event time"),

    REGISTRATION_NOT_OPEN(HttpStatus.BAD_REQUEST, "Event registration is not open"),
    REGISTRATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Event registration is not found"),
    REGISTRATION_DEADLINE_PASSED(HttpStatus.CONFLICT, "Registration deadline has passed"),
    EVENT_FULL(HttpStatus.CONFLICT, "Event is full"),
    REGISTRATION_CANNOT_CANCEL(HttpStatus.CONFLICT, "Registration cannot be cancelled"),
    REGISTRATION_ALREADY_REGISTERED(HttpStatus.CONFLICT, "Already registered for this event"),
    REGISTRATION_ALREADY_CANCELLED(HttpStatus.CONFLICT, "Registration has already been cancelled"),
    REGISTRATION_ALREADY_CHECKED_IN(HttpStatus.CONFLICT, "A checked-in registration cannot be cancelled"),

    CHECK_IN_CODE_INVALID(HttpStatus.BAD_REQUEST, "Invalid check-in code"),
    CHECK_IN_NOT_ALLOWED(HttpStatus.CONFLICT, "Check-in is not allowed"),
    ALREADY_CHECKED_IN(HttpStatus.CONFLICT, "Already check in"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

    TOO_MANY_OTP_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "Too many OTP verification attempts"),
    INVALID_RESET_TOKEN(HttpStatus.BAD_REQUEST, "Invalid or expired reset token"),
    INVALID_OTP(HttpStatus.BAD_REQUEST, "Invalid or expired OTP"),
    TOO_MANY_OTP_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many OTP requests"),

    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "Email has not been verified"),
    TOO_MANY_VERIFICATION_EMAIL_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "Too many verification email request"),

    INVALID_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "Current password is incorrect"),

    CANNOT_MODIFY_YOURSELF(HttpStatus.FORBIDDEN, "Cannot modify your own role or status"),
    CANNOT_MODIFY_OTHER_ADMIN(HttpStatus.FORBIDDEN, "Cannot modify another administrator"),

    INVALID_EMAIL_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "Invalid or expired email verification token");

    private final HttpStatus status;
    private final String defaultMessage;
}
