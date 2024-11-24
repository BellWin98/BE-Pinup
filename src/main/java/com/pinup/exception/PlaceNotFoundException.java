package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.NewErrorCode;

public class PlaceNotFoundException extends BusinessException {

    public PlaceNotFoundException() {
        super(NewErrorCode.PLACE_NOT_FOUND);
    }
}
