package com.pinup.domain.friend;

import com.pinup.entity.Member;
import com.pinup.global.common.BaseTimeEntity;
import com.pinup.global.enums.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    public FriendRequest(FriendRequestStatus friendRequestStatus, Member sender, Member receiver) {
        this.friendRequestStatus = friendRequestStatus;
        this.sender = sender;
        this.receiver = receiver;
    }

}
