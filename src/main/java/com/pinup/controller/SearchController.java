package com.pinup.controller;

import com.pinup.dto.response.search.MemberSearchResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/members")
    public ApiSuccessResponse<List<MemberSearchResponse>> searchMembers(@RequestParam(required = true) String query) {
        List<MemberSearchResponse> searchResult = searchService.searchUsers(query);
        return ApiSuccessResponse.from(searchResult);
    }

}
