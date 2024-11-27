package com.pinup.controller;

import com.pinup.dto.request.BioUpdateRequest;
import com.pinup.dto.request.NicknameUpdateRequest;
import com.pinup.dto.response.ProfileResponse;
import com.pinup.dto.response.MemberResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/search")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> searchMembers(@RequestParam("query") String query) {
        MemberResponse searchResult = memberService.searchUsers(query);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(searchResult));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> getCurrentMemberInfo() {
        MemberResponse currentMember = memberService.getCurrentMemberInfo();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(currentMember));
    }

    @GetMapping("/{friendId}")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> getMemberInfo(@PathVariable("friendId") Long friendId) {
        MemberResponse currentMember = memberService.getMemberInfo(friendId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(currentMember));
    }

    @DeleteMapping
    public  ResponseEntity<ApiSuccessResponse<Void>> deleteMember() {
        memberService.deleteMember();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/nickname/check")
    public ResponseEntity<ApiSuccessResponse<Boolean>> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        boolean isDuplicate = memberService.checkNicknameDuplicate(nickname);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(isDuplicate));
    }

    @PutMapping("/bio")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> updateBio(@RequestBody BioUpdateRequest request) {
        MemberResponse response = memberService.updateBio(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(response));
    }

    @PutMapping("/nickname")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> updateNickname(@RequestBody NicknameUpdateRequest request) {
        MemberResponse response = memberService.updateNickname(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(response));
    }

    @PutMapping("/profile-image")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> updateProfileImage(@RequestParam(value = "multipartFiles") MultipartFile image) {
        MemberResponse response = memberService.updateProfileImage(image);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(response));
    }

    @GetMapping("/me/profile")
    public ResponseEntity<ApiSuccessResponse<ProfileResponse>> getMyFeed() {
        ProfileResponse response = memberService.getMyProfile();
        return ResponseEntity.ok(ApiSuccessResponse.from(response));
    }

    @GetMapping("/{memberId}/profile")
    public ResponseEntity<ApiSuccessResponse<ProfileResponse>> getFeed(@PathVariable Long memberId) {
        ProfileResponse response = memberService.getProfile(memberId);
        return ResponseEntity.ok(ApiSuccessResponse.from(response));
    }
}
