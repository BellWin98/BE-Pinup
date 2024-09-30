package com.pinup.entity;

import com.pinup.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long hostId; // 방장 ID

    private String name; // 채팅방 명

    public ChatRoom(Long hostId, String name) {
        this.hostId = hostId;
        this.name = name;
    }

    public void updateChatRoom(Long hostId, String name) {
        this.hostId = hostId;
        this.name = name;
    }
}
