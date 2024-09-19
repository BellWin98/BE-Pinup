package com.pinup.controller;

import com.pinup.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final JwtUtil jwtUtil;

    @GetMapping("/")
    public String test() {
        return "success";
    }

    @GetMapping("/login-test")
    public String loginTest(){
        return "success";
    }

    @GetMapping("jwt-test")
    public String jwtTest(@RequestHeader("Authorization") String authorizationHeader){
        System.out.println(jwtUtil.getUserIdFromAuthorizationHeader(authorizationHeader));
        return "success";
    }
}
