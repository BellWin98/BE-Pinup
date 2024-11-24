package com.pinup.global.exception;

import com.pinup.global.response.ErrorResponse;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BusinessException extends RuntimeException{

    private final NewErrorCode errorCode;
    private List<ErrorResponse.CustomFieldError> errors = new ArrayList<>();

    public BusinessException(String message, NewErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(NewErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(NewErrorCode errorCode, List<ErrorResponse.CustomFieldError> errors) {
        super(errorCode.getMessage());
        this.errors = errors;
        this.errorCode = errorCode;
    }
}