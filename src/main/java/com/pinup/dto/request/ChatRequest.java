package com.pinup.dto.request;

import com.pinup.entity.Chat;
import com.pinup.entity.ChatRoom;
import com.pinup.enums.MessageType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRequest {

    private Long chatRoomId;
    private Long senderId;
    private String message;
    private MessageType messageType;

    public Chat toEntity(ChatRoom chatRoom) {
        return Chat.builder()
                .chatRoom(chatRoom)
                .senderId(senderId)
                .message(message)
                .messageType(messageType)
                .build();
    }
}