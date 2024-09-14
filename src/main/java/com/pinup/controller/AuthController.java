package com.pinup.controller;


import com.pinup.dto.request.token.TokenResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/google")
    public void googleLogin(HttpServletResponse response) throws IOException {
        String authorizationUrl = authService.getGoogleAuthorizationUrl();
        response.sendRedirect(authorizationUrl);
    }

    @GetMapping("/login/google/callback")
    public ApiSuccessResponse<TokenResponse> googleCallback(@RequestParam String code) {
        TokenResponse tokenResponse = authService.googleLogin(code);
        return ApiSuccessResponse.from(tokenResponse);
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
}