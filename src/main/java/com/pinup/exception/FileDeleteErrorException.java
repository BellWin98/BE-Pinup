package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class FileDeleteErrorException extends BusinessException {

    public FileDeleteErrorException() {
        super(ErrorCode.FILE_DELETE_ERROR);
    }
}
