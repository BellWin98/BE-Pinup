package com.pinup.controller;

import com.pinup.dto.request.SendFriendRequest;
import com.pinup.dto.response.FriendRequestResponse;
import com.pinup.global.response.ResultCode;
import com.pinup.global.response.ResultResponse;
import com.pinup.service.FriendRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend-requests")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/send")
    public ResponseEntity<ResultResponse> sendFriendRequest(
            @RequestBody SendFriendRequest sendFriendRequest) {
        FriendRequestResponse result = friendRequestService.sendFriendRequest(sendFriendRequest.getReceiverId());

        return ResponseEntity.ok(ResultResponse.of(ResultCode.REQUEST_PIN_BUDDY_SUCCESS, result));
    }

    @PatchMapping("/{friendRequestId}/accept")
    public ResponseEntity<ResultResponse> acceptFriendRequest(
            @PathVariable("friendRequestId") Long friendRequestId) {
        FriendRequestResponse result = friendRequestService.acceptFriendRequest(friendRequestId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.ACCEPT_PIN_BUDDY_SUCCESS, result));
    }

    @PatchMapping("/{friendRequestId}/reject")
    public ResponseEntity<ResultResponse> rejectFriendRequest(
            @PathVariable("friendRequestId") Long friendRequestId) {
        FriendRequestResponse result = friendRequestService.rejectFriendRequest(friendRequestId);

        return ResponseEntity.ok(ResultResponse.of(ResultCode.REJECT_PIN_BUDDY_SUCCESS, result));
    }

    @GetMapping("/received")
    public ResponseEntity<ResultResponse> getReceivedFriendRequests() {
        List<FriendRequestResponse> results = friendRequestService.getReceivedFriendRequests();

        return ResponseEntity.ok(ResultResponse.of(ResultCode.GET_PIN_BUDDY_LIST_SUCCESS, results));
    }
}
