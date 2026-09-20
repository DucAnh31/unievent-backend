package com.ducanh.unievent.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{
    private ErrorCode errorCode;

    public ApiException(ErrorCode errorCode)
    {
        super(errorCode.getDefaultMessage());
        this.errorCode = errorCode;
    }
    public ApiException(ErrorCode errorCode, String message)
    {
        super(message);
        this.errorCode = errorCode;
    }
}
