package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.NewErrorCode;

public class ImagesLimitExceededException extends BusinessException {

    public ImagesLimitExceededException() {
        super(NewErrorCode.IMAGES_LIMIT_EXCEEDED);
    }
}
