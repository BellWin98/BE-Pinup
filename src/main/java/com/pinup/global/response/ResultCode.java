package com.pinup.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResultCode {

    // Auth
    LOGIN_SUCCESS(200, "AU001", "로그인에 성공하였습니다."),

    // Place
    GET_PLACES_SUCCESS(200, "P001", "장소 목록 조회에 성공하였습니다."),
    GET_PLACE_DETAIL_SUCCESS(200, "P002", "장소 상세 조회에 성공하였습니다."),

    // Review
    CREATE_REVIEW_SUCCESS(201, "R001", "리뷰 등록에 성공하였습니다."),

    // Friend
    REQUEST_PIN_BUDDY_SUCCESS(200, "F001", "핀버디 신청이 완료되었습니다."),
    ACCEPT_PIN_BUDDY_SUCCESS(200, "F002", "핀버디 신청을 수락했습니다."),
    REJECT_PIN_BUDDY_SUCCESS(200, "F003", "핀버디 신청을 거절했습니다."),
    CANCEL_PIN_BUDDY_SUCCESS(200, "F004", "핀버디 신청을 취소했습니다."),
    REMOVE_PIN_BUDDY_SUCCESS(200, "F005", "해당 핀버디가 삭제되었습니다."),
    GET_PIN_BUDDY_LIST_SUCCESS(200, "F006", "핀버디 목록을 조회하였습니다."),
    GET_MY_PIN_BUDDY_LIST_SUCCESS(200, "F007", "나의 핀버디 목록을 조회하였습니다."),
    GET_USER_PIN_BUDDY_LIST_SUCCESS(200, "F008", "해당 유저의 핀버디 목록을 조회하였습니다."),
    GET_MY_PIN_BUDDY_INFO_SUCCESS(200, "F008", "나의 핀버디 정보를 조회하였습니다."),

    // Alarm
    GET_ALARMS_SUCCESS(200, "AL001", "알림 목록을 조회하였습니다."),
    GET_ALARM_DETAIL_SUCCESS(200, "AL002", "알림 상세 내용을 조회하였습니다."),

    // Article
    CREATE_ARTICLE_SUCCESS(201, "AR001", "에디터 아티클을 생성하였습니다."),
    GET_ARTICLE_DETAIL_SUCCESS(200, "AR002", "에디터 아티클을 조회하였습니다."),
    GET_ARTICLE_LIST_SUCCESS(200, "AR003", "에디터 아티클 목록을 조회하였습니다."),
    ;

    private final int status;
    private final String code;
    private final String message;
}
