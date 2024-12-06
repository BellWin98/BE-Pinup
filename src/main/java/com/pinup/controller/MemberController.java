package com.pinup.controller;

import com.pinup.dto.request.MemberInfoUpdateRequest;
import com.pinup.dto.response.MemberResponse;
import com.pinup.dto.response.ProfileResponse;
import com.pinup.global.response.ResultCode;
import com.pinup.global.response.ResultResponse;
import com.pinup.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/search")
    public ResponseEntity<ResultResponse> searchMembers(@RequestParam("query") String query) {
        MemberResponse searchResult = memberService.searchUsers(query);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MEMBERS_SUCCESS, searchResult));
    }

    @GetMapping("/me")
    public ResponseEntity<ResultResponse> getCurrentMemberInfo() {
        MemberResponse currentMember = memberService.getCurrentMemberInfo();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_LOGIN_USER_INFO_SUCCESS, currentMember));
    }

    @GetMapping("/{friendId}")
    public ResponseEntity<ResultResponse> getMemberInfo(@PathVariable("friendId") Long friendId) {
        MemberResponse currentMember = memberService.getMemberInfo(friendId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_USER_INFO_SUCCESS, currentMember));
    }

    @DeleteMapping
    public ResponseEntity<ResultResponse> deleteMember() {
        memberService.deleteMember();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.DELETE_USER_SUCCESS));
    }

    @GetMapping("/nickname/check")
    public ResponseEntity<ResultResponse> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        boolean isDuplicate = memberService.checkNicknameDuplicate(nickname);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_NICKNAME_DUPLICATE_SUCCESS, isDuplicate));
    }

    @PutMapping
    public ResponseEntity<ResultResponse> updateMemberInfo(@Valid MemberInfoUpdateRequest request,
                                                           @RequestParam(value = "multipartFiles") MultipartFile image
    ) {
        MemberResponse response = memberService.updateMemberInfo(request, image);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.UPDATE_MEMBER_INFO_SUCCESS, response));
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ResultResponse> getMyFeed() {
        ProfileResponse response = memberService.getMyProfile();
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MY_FEED_SUCCESS, response));
    }

    @GetMapping("/{memberId}/profile")
    public ResponseEntity<ResultResponse> getFeed(@PathVariable Long memberId) {
        ProfileResponse response = memberService.getProfile(memberId);
        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_USER_FEED_SUCCESS, response));
    }
}
