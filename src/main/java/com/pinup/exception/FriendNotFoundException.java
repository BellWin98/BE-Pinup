package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class FriendNotFoundException extends BusinessException {

    public FriendNotFoundException() {
        super(ErrorCode.FRIEND_NOT_FOUND);
    }
}
