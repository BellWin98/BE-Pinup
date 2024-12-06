package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class InvalidFileUrlException extends BusinessException {

    public InvalidFileUrlException() {
        super(ErrorCode.INVALID_FILE_URL);
    }
}
