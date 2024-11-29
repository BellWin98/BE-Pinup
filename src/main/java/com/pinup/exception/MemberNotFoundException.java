package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.NewErrorCode;

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException() {
        super(NewErrorCode.MEMBER_NOT_FOUND);
    }
}
