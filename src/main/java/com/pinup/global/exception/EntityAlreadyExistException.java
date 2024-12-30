package com.pinup.global.exception;

public class EntityAlreadyExistException extends BusinessException{
    public EntityAlreadyExistException(ErrorCode errorCode) {
        super(errorCode);
    }
}
