package com.pinup.controller;

import com.pinup.dto.response.MemberSearchResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendShipController {

    private final FriendShipService friendShipService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiSuccessResponse<List<MemberSearchResponse>>> getAllFriends(
            @PathVariable Long userId) {
        List<MemberSearchResponse> result = friendShipService.getAllFriends(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }
}
