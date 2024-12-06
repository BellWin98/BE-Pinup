package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class UnauthorizedAlarmAccessException extends BusinessException {

    public UnauthorizedAlarmAccessException() {
        super(ErrorCode.UNAUTHORIZED_ALARM_ACCESS);
    }
}
