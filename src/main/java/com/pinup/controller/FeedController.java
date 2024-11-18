package com.pinup.controller;

import com.pinup.dto.request.BioUpdateRequest;
import com.pinup.dto.request.NicknameUpdateRequest;
import com.pinup.dto.response.MemberResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.FeedService;
import com.pinup.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/nickname/check")
    public ResponseEntity<ApiSuccessResponse<Boolean>> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        boolean isDuplicate = feedService.checkNicknameDuplicate(nickname);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(isDuplicate));
    }

    @PutMapping("/bio")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> updateBio(@RequestBody BioUpdateRequest request) {
        MemberResponse response = feedService.updateBio(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(response));
    }

    @PutMapping("/nickname")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> updateNickname(@RequestBody NicknameUpdateRequest request) {
        MemberResponse response = feedService.updateNickname(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(response));
    }

    @PutMapping("/profile-image")
    public ResponseEntity<ApiSuccessResponse<MemberResponse>> updateProfileImage(@RequestParam(value = "multipartFiles") MultipartFile image) {
        MemberResponse response = feedService.updateProfileImage(image);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(response));
    }
}
