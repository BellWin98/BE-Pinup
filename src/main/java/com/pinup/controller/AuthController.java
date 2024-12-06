package com.pinup.controller;


import com.pinup.dto.NormalLoginRequest;
import com.pinup.dto.request.MemberJoinRequest;
import com.pinup.global.response.ResultCode;
import com.pinup.global.response.ResultResponse;
import com.pinup.global.response.TokenResponse;
import com.pinup.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
        return ResponseEntity.ok(ResultResponse.of(ResultCode.SOCIAL_LOGIN_SUCCESS, tokenResponse));
    }

    @GetMapping("/tokens")
    public ResponseEntity<ResultResponse> getTokens(HttpServletRequest request) {
        TokenResponse tokenResponse = authService.getTokens(request);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.TOKEN_ISSUED_SUCCESS, tokenResponse));

    }

    @PostMapping("/refresh")
    public ResponseEntity<ResultResponse> refresh(@RequestHeader("Authorization") String refreshToken) {
        TokenResponse tokenResponse = authService.refresh(refreshToken);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.TOKEN_REISSUED_SUCCESS, tokenResponse));
    }

    @PostMapping("/logout")
    public ResponseEntity<ResultResponse> logout(@RequestHeader("Authorization") String accessToken) {
        authService.logout(accessToken);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.LOGOUT_SUCCESS));
    }

    @PostMapping("/join")
    public ResponseEntity<ResultResponse> memberJoin(@RequestBody MemberJoinRequest request) {
        authService.join(request);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.SIGN_UP_SUCCESS));
    }

    @PostMapping("/login/normal")
    public ResponseEntity<ResultResponse> normalLogin(@RequestBody NormalLoginRequest request) {
        TokenResponse tokenResponse = authService.normalLogin(request);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.NORMAL_LOGIN_SUCCESS, tokenResponse));
    }
}