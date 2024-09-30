package com.pinup.entity;

import com.pinup.enums.MessageType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public Chat( ChatRoom chatRoom, Long senderId, String message, MessageType messageType) {
        this.chatRoom = chatRoom;
        this.senderId = senderId;
        this.message = message;
        this.messageType = messageType;
        this.sendTime = LocalDateTime.now();
    }
}
