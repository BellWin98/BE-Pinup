package com.pinup.controller;

import com.pinup.dto.request.ChatRequest;
import com.pinup.dto.response.ChatResponse;
import com.pinup.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/{roomId}") // 이 경로로 전송되면 메서드 호출 -> WebSocketConfig prefixes에서 적용한 건 앞에 생략
    @SendTo("/topic/{roomId}") // 구독하고 있는 장소로 메시지 전송(목적지) -> WebSocketConfig Broker에서 적용한건 앞에 붙여주기
    public ChatResponse sendMessage(@DestinationVariable Long chatRoomId,
                                    @Payload ChatRequest chatRequest) {

        return null;
    }

}
