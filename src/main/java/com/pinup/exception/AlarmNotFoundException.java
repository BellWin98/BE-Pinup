package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.NewErrorCode;

public class AlarmNotFoundException extends BusinessException {

    public AlarmNotFoundException() {
        super(NewErrorCode.ALARM_NOT_FOUND);
    }
}
