package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class AlarmNotFoundException extends BusinessException {

    public AlarmNotFoundException() {
        super(ErrorCode.ALARM_NOT_FOUND);
    }
}
