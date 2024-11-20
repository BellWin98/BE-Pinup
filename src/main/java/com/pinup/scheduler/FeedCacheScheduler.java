package com.pinup.scheduler;

import com.pinup.cache.RedisFeedCache;
import com.pinup.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedCacheScheduler {
    private final RedisFeedCache redisFeedCache;
    private final FeedService feedService;

    @Scheduled(fixedRate = 60000)
    public void syncFeedsToDatabase() {
        redisFeedCache.syncToDatabase(feedService::buildFeedResponse);
    }
}