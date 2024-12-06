package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class NicknameUpdateTimeLimitException extends BusinessException {

    public NicknameUpdateTimeLimitException() {
        super(ErrorCode.NICKNAME_UPDATE_TIME_LIMIT);
    }
}
