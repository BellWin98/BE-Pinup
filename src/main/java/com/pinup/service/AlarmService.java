package com.pinup.service;

import com.pinup.dto.response.AlarmResponse;
import com.pinup.entity.Alarm;
import com.pinup.entity.Member;
import com.pinup.repository.AlarmRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.pinup.global.exception.PinUpException.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final MemberRepository memberRepository;

    public Page<AlarmResponse> getMyAlarms(Pageable pageable) {
        Member currentMember = getCurrentMember();
        return alarmRepository.findAllByReceiver(currentMember, pageable)
                .map(AlarmResponse::from);
    }

    public AlarmResponse getAlarm(Long alarmId) {
        Member currentMember = getCurrentMember();
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> ALARM_NOT_FOUND);

        if (!alarm.getReceiver().equals(currentMember)) {
            throw UNAUTHORIZED_ALARM_ACCESS;
        }
        alarm.read();
        alarmRepository.save(alarm);
        return AlarmResponse.from(alarm);
    }

    private Member getCurrentMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> MEMBER_NOT_FOUND);
    }
}