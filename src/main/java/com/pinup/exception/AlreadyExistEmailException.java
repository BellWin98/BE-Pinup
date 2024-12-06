package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class AlreadyExistEmailException extends BusinessException {

    public AlreadyExistEmailException() {
        super(ErrorCode.ALREADY_EXIST_EMAIL);
    }
}
