package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class SseConnectionErrorException extends BusinessException {

    public SseConnectionErrorException() {
        super(ErrorCode.SSE_CONNECTION_ERROR);
    }
}
