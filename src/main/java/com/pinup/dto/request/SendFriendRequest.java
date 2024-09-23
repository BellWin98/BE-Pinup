package com.pinup.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SendFriendRequest {
    private Long receiverId;
}
