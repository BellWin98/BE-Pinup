package com.pinup.controller;

import com.pinup.dto.response.MemberResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/search")
    public ApiSuccessResponse<List<MemberResponse>> searchMembers(@RequestParam("query") String query) {
        List<MemberResponse> searchResult = memberService.searchUsers(query);
        return ApiSuccessResponse.from(searchResult);
    }

}
