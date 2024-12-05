package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.NewErrorCode;

public class FriendNotFoundException extends BusinessException {

    public FriendNotFoundException() {
        super(NewErrorCode.FRIEND_NOT_FOUND);
    }
}
