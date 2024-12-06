package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class SocialLoginTokenNotFoundException extends BusinessException {

    public SocialLoginTokenNotFoundException() {
        super(ErrorCode.SOCIAL_LOGIN_TOKEN_NOT_FOUND);
    }
}
