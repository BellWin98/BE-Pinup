package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class AlreadyProcessedFriendRequestException extends BusinessException {

    public AlreadyProcessedFriendRequestException() {
        super(ErrorCode.ALREADY_PROCESSED_FRIEND_REQUEST);
    }
}
