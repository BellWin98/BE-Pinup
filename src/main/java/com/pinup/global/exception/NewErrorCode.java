package com.pinup.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum NewErrorCode {

    // Global
    INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
    METHOD_NOT_ALLOWED(405, "G002", "허용되지 않은 HTTP method입니다."),
    INPUT_VALUE_INVALID(400, "G003", "유효하지 않은 입력입니다."),
    INPUT_TYPE_INVALID(400, "G004", "입력 타입이 유효하지 않습니다."),
    HTTP_MESSAGE_NOT_READABLE(400, "G005", "request message body가 없거나, 값 타입이 올바르지 않습니다."),
    HTTP_HEADER_INVALID(400, "G006", "request header가 유효하지 않습니다."),
    IMAGE_TYPE_NOT_SUPPORTED(400, "G007", "지원하지 않는 이미지 타입입니다."),
    FILE_CONVERT_FAIL(500, "G008", "변환할 수 없는 파일입니다."),
    ENTITY_TYPE_INVALID(500, "G009", "올바르지 않은 entity type 입니다."),
    FILTER_MUST_RESPOND(500, "G010", "필터에서 처리해야 할 요청이 Controller에 접근하였습니다."),

    // Member
    MEMBER_NOT_FOUND(400, "M001", "존재하지 않는 유저입니다."),

    // Review
    IMAGES_LIMIT_EXCEEDED(400, "R001", "등록 가능한 이미지 갯수를 초과했습니다."),
    REVIEW_NOT_FOUND(404, "R002", "존재하지 않는 리뷰입니다."),

    // Place
    PLACE_NOT_FOUND(404, "P001", "존재하지 않는 장소입니다."),

    // Friend
    ALREADY_EXIST_FRIEND_REQUEST(400, "F001", "이미 존재하는 친구 요청입니다."),
    SELF_FRIEND_REQUEST(400, "F002", "자기 자신에게 친구 요청을 보낼 수 없습니다."),
    ALREADY_PROCESSED_FRIEND_REQUEST(400, "F003", "이미 처리된 친구 요청입니다."),
    FRIEND_REQUEST_NOT_FOUND(404, "F004", "존재하지 않는 친구 요청입니다."),
    FRIENDSHIP_NOT_FOUND(404, "F005", "존재하지 않는 친구 관계입니다."),
    FRIEND_NOT_FOUND(404, "F006", "해당 이름을 가진 친구를 찾을 수 없습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
