package com.pinup.dto.response;

import com.pinup.entity.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FriendRequestResponse {

    private Long id;
    private String friendRequestStatus;
    private MemberResponse sender;
    private MemberResponse receiver;

    public static FriendRequestResponse from(FriendRequest friendRequest) {
        return FriendRequestResponse.builder()
                .id(friendRequest.getId())
                .friendRequestStatus(friendRequest.getFriendRequestStatus().name())
                .sender(MemberResponse.from(friendRequest.getSender()))
                .receiver(MemberResponse.from(friendRequest.getReceiver()))
                .build();
    }
}
