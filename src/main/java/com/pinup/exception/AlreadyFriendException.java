package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class AlreadyFriendException extends BusinessException {

    public AlreadyFriendException() {
        super(ErrorCode.ALREADY_FRIEND);
    }
}
