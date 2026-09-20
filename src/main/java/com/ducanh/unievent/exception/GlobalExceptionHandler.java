package com.ducanh.unievent.exception;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.ErrorDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException apiException)
    {
        return ResponseEntity
                .status(apiException.getErrorCode().getStatus())
                .body(ApiResponse.error(
                        apiException.getMessage(),
                        ErrorDetail.of(apiException.getErrorCode().name())));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException badCredentialsException)
    {
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getStatus())
                .body(ApiResponse.error(
                        ErrorCode.UNAUTHORIZED.getDefaultMessage(),
                        ErrorDetail.of(ErrorCode.UNAUTHORIZED.name())));
    }
}
