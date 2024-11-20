package com.pinup.scheduler;

import com.pinup.service.MemberCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberCacheScheduler {
    private final MemberCacheService memberCacheService;

    @Scheduled(fixedRate = 300000)
    public void syncNicknamesToDatabase() {
        memberCacheService.syncToDatabase();
    }
}