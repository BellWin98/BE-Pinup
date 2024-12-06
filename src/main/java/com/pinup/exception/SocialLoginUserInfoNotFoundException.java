package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class SocialLoginUserInfoNotFoundException extends BusinessException {

    public SocialLoginUserInfoNotFoundException() {
        super(ErrorCode.SOCIAL_LOGIN_USER_INFO_NOT_FOUND);
    }
}
