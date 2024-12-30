package com.pinup.global.response;

import com.pinup.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(title = "소셜 로그인 응답 DTO", description = "소셜 로그인 성공 시 Access/Refresh 토큰 및 유저 정보 반환")
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private MemberResponse memberResponse;
}