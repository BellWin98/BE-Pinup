package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class ReviewNotFoundException extends BusinessException {

    public ReviewNotFoundException() {
        super(ErrorCode.REVIEW_NOT_FOUND);
    }
}