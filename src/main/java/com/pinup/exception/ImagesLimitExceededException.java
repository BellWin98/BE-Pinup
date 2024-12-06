package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class ImagesLimitExceededException extends BusinessException {

    public ImagesLimitExceededException() {
        super(ErrorCode.IMAGES_LIMIT_EXCEEDED);
    }
}
