package com.pinup.controller;

import com.pinup.dto.response.AlarmResponse;
import com.pinup.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    public ResponseEntity<Page<AlarmResponse>> getMyAlarms(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(alarmService.getMyAlarms(pageable));
    }

    @GetMapping("/{alarmId}")
    public ResponseEntity<AlarmResponse> getAlarm(@PathVariable Long alarmId) {
        return ResponseEntity.ok(alarmService.getAlarm(alarmId));
    }
}
