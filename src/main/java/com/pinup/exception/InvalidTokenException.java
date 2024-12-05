package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.NewErrorCode;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super(NewErrorCode.INVALID_TOKEN);
    }
}
