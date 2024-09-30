package com.pinup.dto.response;

import com.pinup.entity.Chat;
import com.pinup.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class ChatResponse {

    private Long chatId;
    private Long chatRoomId;
    private Long senderId;
    private MessageType messageType;
    private String message;
    private String sendTime;

    public static ChatResponse from(Chat chat) {

        return ChatResponse.builder()
                .chatId(chat.getId())
                .chatRoomId(chat.getChatRoom().getId())
                .senderId(chat.getSenderId())
                .messageType(chat.getMessageType())
                .message(chat.getMessage())
                .sendTime(chat.getSendTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
