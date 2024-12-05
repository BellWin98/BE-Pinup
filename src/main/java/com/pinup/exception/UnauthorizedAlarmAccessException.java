package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.NewErrorCode;

public class UnauthorizedAlarmAccessException extends BusinessException {

    public UnauthorizedAlarmAccessException() {
        super(NewErrorCode.UNAUTHORIZED_ALARM_ACCESS);
    }
}
