package com.pinup.global.config;

import lombok.Builder;
import lombok.Getter;
import java.time.Duration;

@Getter
@Builder
public class CacheConfig {
    private final String prefix;
    private final Duration ttl;
    private final boolean useCompression;

    public static CacheConfig defaultConfig(String prefix) {
        return CacheConfig.builder()
                .prefix(prefix)
                .ttl(Duration.ofHours(24))
                .useCompression(false)
                .build();
    }
}