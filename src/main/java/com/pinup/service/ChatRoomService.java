package com.pinup.service;

import com.pinup.dto.request.ChatRoomRequest;
import com.pinup.entity.Member;
import com.pinup.global.exception.PinUpException;
import com.pinup.repository.ChatRoomRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public Long createChatRoom(ChatRoomRequest chatRoomRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member findMember = memberRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> PinUpException.MEMBER_NOT_FOUND);

        return chatRoomRepository.save(chatRoomRequest.toEntity(findMember)).getId();
    }
}
