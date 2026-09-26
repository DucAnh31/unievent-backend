package com.ducanh.unievent.exception;

import com.ducanh.unievent.common.ApiResponse;
import com.ducanh.unievent.common.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException apiException)
    {
        log.error("global exception: ",apiException);
        return ResponseEntity
                .status(apiException.getErrorCode().getStatus())
                .body(ApiResponse.error(
                        apiException.getMessage(),
                        ErrorDetail.of(apiException.getErrorCode().name())));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException badCredentialsException)
    {
        log.error("global exception: ",badCredentialsException);
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getStatus())
                .body(ApiResponse.error(
                        ErrorCode.UNAUTHORIZED.getDefaultMessage(),
                        ErrorDetail.of(ErrorCode.UNAUTHORIZED.name())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException methodArgumentNotValidException) {

        log.error("global exception: ",methodArgumentNotValidException);

        List<ErrorDetail.FieldError> fieldErrors = new ArrayList<>();

        for (FieldError fieldError : methodArgumentNotValidException.getBindingResult().getFieldErrors()) {
            fieldErrors.add(toFieldError(fieldError));
        }


        ErrorDetail error = ErrorDetail.of(ErrorCode.VALIDATION_ERROR.name(), fieldErrors);
        return ResponseEntity
                .status(ErrorCode.VALIDATION_ERROR.getStatus())
                .body(ApiResponse.error(ErrorCode.VALIDATION_ERROR.getDefaultMessage(), error));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException exception) {

        return ResponseEntity
                .status(ErrorCode.FORBIDDEN.getStatus())
                .body(ApiResponse.error(ErrorCode.FORBIDDEN.getDefaultMessage(), ErrorDetail.of(ErrorCode.FORBIDDEN.name())));
    }

    private ErrorDetail.FieldError toFieldError(FieldError fe) {
        return new ErrorDetail.FieldError(fe.getField(), fe.getDefaultMessage());
    }
}
