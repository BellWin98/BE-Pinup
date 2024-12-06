package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class AlreadyExistNicknameException extends BusinessException {

    public AlreadyExistNicknameException() {
        super(ErrorCode.ALREADY_EXIST_NICKNAME);
    }
}
