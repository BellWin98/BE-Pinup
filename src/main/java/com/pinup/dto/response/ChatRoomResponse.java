package com.pinup.dto.response;

import com.pinup.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponse {

    private Long chatRoomId;
    private Long hostId;
    private String name;

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .hostId(chatRoom.getHostId())
                .name(chatRoom.getName())
                .build();
    }
}
