package com.ducanh.unievent.common;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ErrorDetail {
    private String code;
    private List<FieldError> details;

    public static ErrorDetail of(String code) {
        return new ErrorDetail(code, null);
    }

    public static ErrorDetail of(String code, List<FieldError> details) {
        return new ErrorDetail(code, details);
    }

    @AllArgsConstructor
    @Getter
    public static class FieldError {
        private String field;
        private String message;
    }
}
