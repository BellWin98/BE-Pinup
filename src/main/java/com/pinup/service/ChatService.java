package com.pinup.service;

import com.pinup.dto.request.ChatRequest;
import com.pinup.entity.ChatRoom;
import com.pinup.global.exception.PinUpException;
import com.pinup.repository.ChatRepository;
import com.pinup.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void saveMessage(ChatRequest chatRequest) {
        ChatRoom findChatRoom = chatRoomRepository
                .findById(chatRequest.getChatRoomId())
                .orElseThrow(() -> PinUpException.CHAT_ROOM_NOT_FOUND);

        chatRepository.save(chatRequest.toEntity(findChatRoom));
    }
}
