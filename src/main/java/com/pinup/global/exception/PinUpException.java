package com.pinup.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class PinUpException extends RuntimeException {

    public static final PinUpException ALREADY_EXIST_NICKNAME = new PinUpException(ErrorCode.ALREADY_EXIST_NICKNAME);
    public static final PinUpException INVALID_TOKEN = new PinUpException(ErrorCode.INVALID_TOKEN);
    public static final PinUpException FORBIDDEN = new PinUpException(ErrorCode.FORBIDDEN);
    public static final PinUpException EXPIRED_OR_PREVIOUS_REFRESH_TOKEN = new PinUpException(ErrorCode.EXPIRED_OR_PREVIOUS_REFRESH_TOKEN);
    public static final PinUpException MEMBER_NOT_FOUND = new PinUpException(ErrorCode.MEMBER_NOT_FOUND);
    public static final PinUpException INTERNAL_SERVER_ERROR = new PinUpException(ErrorCode.INTERNAL_SERVER_ERROR);
    public static final PinUpException VALIDATION_FAILED = new PinUpException(ErrorCode.VALIDATION_FAILED);
    public static final PinUpException ALREADY_EXIST_EMAIL = new PinUpException(ErrorCode.ALREADY_EXIST_EMAIL);
    public static final PinUpException NOT_EXPIRED_ACCESS_TOKEN = new PinUpException(ErrorCode.NOT_EXPIRED_ACCESS_TOKEN);
    public static final PinUpException ACCESS_DENIED = new PinUpException(ErrorCode.ACCESS_DENIED);
    public static final PinUpException EXPIRED_ACCESS_TOKEN = new PinUpException(ErrorCode.EXPIRED_ACCESS_TOKEN);
    public static final PinUpException PLACE_NOT_FOUND = new PinUpException(ErrorCode.PLACE_NOT_FOUND);
    public static final PinUpException REVIEW_NOT_FOUND = new PinUpException(ErrorCode.REVIEW_NOT_FOUND);
    public static final PinUpException FILE_EXTENSION_INVALID = new PinUpException(ErrorCode.FILE_EXTENSION_INVALID);
    public static final PinUpException FRIEND_REQUEST_NOT_FOUND = new PinUpException(ErrorCode.FRIEND_REQUEST_NOT_FOUND);
    public static final PinUpException SSE_CONNECTION_ERROR = new PinUpException(ErrorCode.SSE_CONNECTION_ERROR);
    public static final PinUpException FRIENDSHIP_NOT_FOUND = new PinUpException(ErrorCode.FRIENDSHIP_NOT_FOUND);
    public static final PinUpException IMAGES_LIMIT_EXCEEDED = new PinUpException(ErrorCode.IMAGES_LIMIT_EXCEEDED);
    public static final PinUpException KEYWORDS_LIMIT_EXCEEDED = new PinUpException(ErrorCode.KEYWORDS_LIMIT_EXCEEDED);
    public static final PinUpException KEYWORDS_LENGTH_LIMIT_EXCEEDED = new PinUpException(ErrorCode.KEYWORDS_LENGTH_LIMIT_EXCEEDED);
    public static final PinUpException ALREADY_EXIST_FRIEND_REQUEST = new PinUpException(ErrorCode.ALREADY_EXIST_FRIEND_REQUEST);
    public static final PinUpException FRIEND_REQUEST_RECEIVER_MISMATCH = new PinUpException(ErrorCode.FRIEND_REQUEST_RECEIVER_MISMATCH);
    public static final PinUpException SELF_FRIEND_REQUEST = new PinUpException(ErrorCode.SELF_FRIEND_REQUEST);
    public static final PinUpException ALREADY_PROCESSED_FRIEND_REQUEST = new PinUpException(ErrorCode.ALREADY_PROCESSED_FRIEND_REQUEST);
    public static final PinUpException FRIEND_NOT_FOUND = new PinUpException(ErrorCode.FRIEND_NOT_FOUND);
    public static final PinUpException ALREADY_FRIEND = new PinUpException(ErrorCode.ALREADY_FRIEND);
    public static final PinUpException PASSWORD_MISMATCH = new PinUpException(ErrorCode.PASSWORD_MISMATCH);
    public static final PinUpException NICKNAME_UPDATE_TIME_LIMIT = new PinUpException(ErrorCode.NICKNAME_UPDATE_TIME_LIMIT);
    public static final PinUpException FILE_DELETE_ERROR = new PinUpException(ErrorCode.FILE_DELETE_ERROR);
    public static final PinUpException INVALID_FILE_URL = new PinUpException(ErrorCode.INVALID_FILE_URL);
    public static final PinUpException CACHE_KEY_NULL = new PinUpException(ErrorCode.CACHE_KEY_NULL);
    public static final PinUpException CACHE_SERIALIZATION_ERROR = new PinUpException(ErrorCode.CACHE_SERIALIZATION_ERROR);
    public static final PinUpException CACHE_DESERIALIZATION_ERROR = new PinUpException(ErrorCode.CACHE_DESERIALIZATION_ERROR);
    public static final PinUpException CACHE_OPERATION_ERROR = new PinUpException(ErrorCode.CACHE_OPERATION_ERROR);

    private final ErrorCode errorCode;

    public HttpStatus getHttpStatus() {
        return errorCode.getHttpStatus();
    }
}

