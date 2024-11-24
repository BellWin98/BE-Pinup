package com.pinup.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {

    // Place
    GET_PLACES_SUCCESS(200, "P001", "장소 목록 조회에 성공하였습니다."),

    // Review
    CREATE_REVIEW_SUCCESS(200, "R001", "리뷰 등록에 성공하였습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
