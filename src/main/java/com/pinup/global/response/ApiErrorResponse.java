package com.pinup.global.response;

import com.pinup.global.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiErrorResponse {

    private String code;
    private String message;

    public static ApiErrorResponse from(ErrorCode errorCode) {
        return ApiErrorResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public void changeMessage(String message) {
        this.message = message;
    }
}
