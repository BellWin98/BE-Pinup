package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class FriendRequestNotFoundException extends BusinessException {

    public FriendRequestNotFoundException() {
        super(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
    }
}
