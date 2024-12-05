package com.pinup.controller;


import com.pinup.dto.NormalLoginRequest;
import com.pinup.dto.request.MemberJoinRequest;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.global.response.ResultCode;
import com.pinup.global.response.ResultResponse;
import com.pinup.global.response.TokenResponse;
import com.pinup.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {

        String authorizationUrl = authService.getGoogleAuthorizationUrl();
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/login/google/callback")
    public ResponseEntity<ResultResponse> googleCallback(@RequestParam("code") String code) {

        TokenResponse tokenResponse = authService.googleLogin(code);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.LOGIN_SUCCESS, tokenResponse));
    }

    @GetMapping("/tokens")
    public ResponseEntity<ApiSuccessResponse<TokenResponse>> getTokens(HttpServletRequest request) {
        TokenResponse tokenResponse = authService.getTokens(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(tokenResponse));
    }

    @PostMapping("/refresh")
    public ApiSuccessResponse<TokenResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        return ApiSuccessResponse.from(tokenResponse);
    }

    @PostMapping("/logout")
    public ApiSuccessResponse<?> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return ApiSuccessResponse.NO_DATA_RESPONSE;
    }

    @PostMapping("/join")
    public ResponseEntity<ApiSuccessResponse<?>> memberJoin(@RequestBody MemberJoinRequest request) {
        authService.join(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.NO_DATA_RESPONSE);
    }

    @PostMapping("/login/normal")
    public ResponseEntity<ApiSuccessResponse<TokenResponse>> normalLogin(@RequestBody NormalLoginRequest request) {
        TokenResponse tokenResponse = authService.normalLogin(request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(tokenResponse));
    }

}