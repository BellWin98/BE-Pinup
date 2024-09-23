package com.pinup.entity;

import com.pinup.global.common.BaseTimeEntity;
import com.pinup.global.enums.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.pinup.global.enums.FriendRequestStatus.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FriendRequestStatus friendRequestStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Member receiver;

    @Builder
    public FriendRequest(Member sender, Member receiver) {
        this.friendRequestStatus = PENDING;
        this.sender = sender;
        this.receiver = receiver;
    }

    public void accept() {
        this.friendRequestStatus = ACCEPTED;
    }

    public void reject() {
        this.friendRequestStatus = REJECTED;
    }

}
