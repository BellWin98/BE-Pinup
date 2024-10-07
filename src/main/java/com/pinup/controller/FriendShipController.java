package com.pinup.controller;

import com.pinup.dto.response.MemberResponse;
import com.pinup.global.response.ApiSuccessResponse;
import com.pinup.service.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendShipController {

    private final FriendShipService friendShipService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiSuccessResponse<List<MemberResponse>>> getAllFriends(
            @PathVariable Long userId) {
        List<MemberResponse> result = friendShipService.getAllFriends(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiSuccessResponse.from(result));
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<ApiSuccessResponse<?>> removeFriend(@PathVariable Long friendId) {
        friendShipService.removeFriend(friendId);

        return ResponseEntity.ok(ApiSuccessResponse.NO_DATA_RESPONSE);
    }
}
