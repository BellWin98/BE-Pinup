package com.pinup.service;

import com.pinup.dto.response.AlarmResponse;
import com.pinup.entity.Alarm;
import com.pinup.entity.Member;
import com.pinup.exception.AlarmNotFoundException;
import com.pinup.exception.MemberNotFoundException;
import com.pinup.exception.UnauthorizedAlarmAccessException;
import com.pinup.repository.AlarmRepository;
import com.pinup.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(AlarmNotFoundException::new);

        if (!alarm.getReceiver().equals(currentMember)) {
            throw new UnauthorizedAlarmAccessException();
        }
        alarm.read();
        alarmRepository.save(alarm);
        return AlarmResponse.from(alarm);
    }

    private Member getCurrentMember() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }
}