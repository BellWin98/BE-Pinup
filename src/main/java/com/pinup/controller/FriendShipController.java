package com.pinup.controller;

import com.pinup.dto.response.MemberResponse;
import com.pinup.global.response.ResultCode;
import com.pinup.global.response.ResultResponse;
import com.pinup.service.FriendShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friendships")
@RequiredArgsConstructor
public class FriendShipController {

    private final FriendShipService friendShipService;

    @GetMapping("/{userId}")
    public ResponseEntity<ResultResponse> getAllFriends(
            @PathVariable Long userId) {
        List<MemberResponse> result = friendShipService.getAllFriends(userId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_USER_PIN_BUDDY_LIST_SUCCESS, result));
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<ResultResponse> removeFriend(@PathVariable Long friendId) {

        friendShipService.removeFriend(friendId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.REMOVE_PIN_BUDDY_SUCCESS));
    }

    @GetMapping
    public ResponseEntity<ResultResponse> getAllFriends() {
        List<MemberResponse> result = friendShipService.getAllFriends();

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MY_PIN_BUDDY_LIST_SUCCESS, result));
    }

    @GetMapping("/search")
    public ResponseEntity<ResultResponse> searchMyFriendInfoByNickname(@RequestParam("query") String query) {
        MemberResponse result = friendShipService.searchMyFriendInfoByNickname(query);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_MY_PIN_BUDDY_INFO_SUCCESS, result));
    }
}
