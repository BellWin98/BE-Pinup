package com.pinup.dto.response;

import com.pinup.entity.Alarm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class AlarmResponse {
    private Long id;
    private MemberResponse receiver;
    private boolean isRead;
    private String message;

    public static AlarmResponse from(Alarm alarm){
        return AlarmResponse.builder()
                .id(alarm.getId())
                .receiver(MemberResponse.from(alarm.getReceiver()))
                .isRead(alarm.isRead())
                .message(alarm.getMessage())
                .build();
    }
}