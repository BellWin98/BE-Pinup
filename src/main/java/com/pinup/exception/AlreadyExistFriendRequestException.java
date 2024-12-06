package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class AlreadyExistFriendRequestException extends BusinessException {

    public AlreadyExistFriendRequestException() {
        super(ErrorCode.ALREADY_EXIST_FRIEND_REQUEST);
    }
}
