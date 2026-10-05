package com.ducanh.unievent.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.ErrorDetail;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(ApiResponse.error(
                        e.getMessage(), ErrorDetail.of(e.getErrorCode().name())));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getStatus())
                .body(ApiResponse.error(
                        ErrorCode.UNAUTHORIZED.getDefaultMessage(), ErrorDetail.of(ErrorCode.UNAUTHORIZED.name())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException e) {

        List<ErrorDetail.FieldError> fieldErrors = new ArrayList<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            fieldErrors.add(toFieldError(fieldError));
        }

        ErrorDetail error = ErrorDetail.of(ErrorCode.VALIDATION_ERROR.name(), fieldErrors);
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.VALIDATION_ERROR.getDefaultMessage(), error));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException e) {

        return ResponseEntity.status(ErrorCode.FORBIDDEN.getStatus())
                .body(ApiResponse.error(
                        ErrorCode.FORBIDDEN.getDefaultMessage(), ErrorDetail.of(ErrorCode.FORBIDDEN.name())));
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("Uncategorized exception: ", e);

        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatus())
                .body(ApiResponse.error(
                        ErrorCode.UNCATEGORIZED_EXCEPTION.getDefaultMessage(),
                        ErrorDetail.of(ErrorCode.UNCATEGORIZED_EXCEPTION.name())));
    }

    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<Void>> handleInvalidRequestFormat(Exception e) {
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.error(
                        ErrorCode.VALIDATION_ERROR.getDefaultMessage(),
                        ErrorDetail.of(ErrorCode.VALIDATION_ERROR.name())));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Void>> handleDisabledException(DisabledException e) {

        return ResponseEntity.status(ErrorCode.ACCOUNT_DISABLED.getStatus())
                .body(ApiResponse.error(
                        ErrorCode.ACCOUNT_DISABLED.getDefaultMessage(),
                        ErrorDetail.of(ErrorCode.ACCOUNT_DISABLED.name())));
    }

    private ErrorDetail.FieldError toFieldError(FieldError fe) {
        return new ErrorDetail.FieldError(fe.getField(), fe.getDefaultMessage());
    }
}
