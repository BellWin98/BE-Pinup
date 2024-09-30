package com.pinup.dto.request;

import com.pinup.entity.ChatRoom;
import com.pinup.entity.Member;
import lombok.Data;

@Data
public class ChatRoomRequest {

    private String name;

    public ChatRoom toEntity(Member member) {
        return new ChatRoom(member.getId(), name);
    }
}
