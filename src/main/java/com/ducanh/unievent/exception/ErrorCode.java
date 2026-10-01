package com.ducanh.unievent.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Uncategorized error"),

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
    EVENT_CANNOT_BE_SUMMITED(HttpStatus.BAD_REQUEST, "Event cannot be summited"),
    EVENT_CANNOT_BE_APPROVED(HttpStatus.BAD_REQUEST, "Event cannot be approved"),
    EVENT_CANNOT_BE_REJECTED(HttpStatus.BAD_REQUEST, "Event cannot be rejected"),
    INVALID_EVENT_TIME(HttpStatus.BAD_REQUEST, "Invalid event time"),

    REGISTRATION_NOT_OPEN(HttpStatus.BAD_REQUEST, "Event registration is not open"),
    REGISTRATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "Event registration is not found"),
    REGISTRATION_DEADLINE_PASSED(HttpStatus.BAD_REQUEST, "Registration deadline has passed"),
    EVENT_FULL(HttpStatus.BAD_REQUEST, "Event is full"),
    REGISTRATION_CANNOT_CANCEL(HttpStatus.BAD_REQUEST, "Cannot cancel registration"),
    REGISTRATION_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "Already registered for this event"),
    REGISTRATION_ALREADY_CANCELLED(HttpStatus.BAD_REQUEST, "Already cancelled for this event"),

    CHECK_IN_CODE_INVALID(HttpStatus.BAD_REQUEST, "Check in code invalid"),
    CHECK_IN_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Check in not allowed"),
    ALREADY_CHECKED_IN(HttpStatus.BAD_REQUEST, "Already check in"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

    TOO_MANY_OTP_ATTEMPTS(HttpStatus.BAD_REQUEST, "Too many otp attempts"),
    INVALID_RESET_TOKEN(HttpStatus.BAD_REQUEST, "Reset token invalid"),
    INVALID_OTP(HttpStatus.BAD_REQUEST, "OTP invalid"),
    TOO_MANY_OTP_REQUESTS(HttpStatus.BAD_REQUEST, "Too many otp requests"),

    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "Email not verified"),
    TOO_MANY_VERIFICATION_EMAIL_REQUESTS(HttpStatus.BAD_REQUEST, "Too many verification email request"),

    INVALID_CURRENT_PASSWORD(HttpStatus.BAD_REQUEST, "Current password invalid"),

    CANNOT_MODIFY_YOURSELF(HttpStatus.BAD_REQUEST, "Cannot modify yourself"),
    CANNOT_MODIFY_OTHER_ADMIN(HttpStatus.BAD_REQUEST, "Cannot mofify other admin"),

    INVALID_EMAIL_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "Email verification token invalid");

    private final HttpStatus status;
    private final String defaultMessage;
}
