package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.ErrorCode;

public class FileExtensionInvalidException extends BusinessException {

    public FileExtensionInvalidException() {
        super(ErrorCode.FILE_EXTENSION_INVALID);
    }
}
