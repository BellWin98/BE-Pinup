package com.pinup.dto.response;

import com.pinup.entity.FriendRequest;
import com.pinup.global.enums.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FriendRequestResponse {

    private Long id;

    private FriendRequestStatus friendRequestStatus;

    private MemberSearchResponse sender;

    private MemberSearchResponse receiver;

    public static FriendRequestResponse from(FriendRequest friendRequest) {
        return FriendRequestResponse.builder()
                .id(friendRequest.getId())
                .friendRequestStatus(friendRequest.getFriendRequestStatus())
                .sender(MemberSearchResponse.from(friendRequest.getSender()))
                .receiver(MemberSearchResponse.from(friendRequest.getReceiver()))
                .build();
    }
}
