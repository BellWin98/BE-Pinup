package com.pinup.exception;

import com.pinup.global.exception.BusinessException;
import com.pinup.global.exception.NewErrorCode;

public class ArticleNotFoundException extends BusinessException {

    public ArticleNotFoundException() {
        super(NewErrorCode.ARTICLE_NOT_FOUND);
    }
}
