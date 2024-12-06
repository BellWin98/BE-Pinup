package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class SelfFriendRequestException extends BusinessException {

    public SelfFriendRequestException() {
        super(ErrorCode.SELF_FRIEND_REQUEST);
    }
}
