package com.pinup.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinup.dto.response.FeedResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.Base64;
import java.util.Set;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisFeedCache {
    private static final String FEED_CACHE_PREFIX = "feed:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public FeedResponse getOrCreateFeed(Long memberId, Function<Long, FeedResponse> feedBuilder) {
        String cacheKey = FEED_CACHE_PREFIX + memberId;
        String compressedCache = redisTemplate.opsForValue().get(cacheKey);

        if (compressedCache != null) {
            try {
                return deserializeAndDecompress(compressedCache);
            } catch (Exception e) {
                log.error("피드 캐시 압축 해제 중 오류 발생. 회원 ID: {}", memberId, e);
                invalidateCache(memberId);
            }
        }

        FeedResponse feed = feedBuilder.apply(memberId);
        cacheFeed(memberId, feed);
        return feed;
    }

    public void cacheFeed(Long memberId, FeedResponse feed) {
        try {
            String compressedFeed = compressAndSerialize(feed);
            redisTemplate.opsForValue().set(
                    FEED_CACHE_PREFIX + memberId,
                    compressedFeed,
                    CACHE_TTL
            );
        } catch (Exception e) {
            log.error("feed를 캐싱하는 중 에러 발생 멤버 ID: {}", memberId, e);
        }
    }

    private String compressAndSerialize(FeedResponse feed) throws Exception {
        String jsonFeed = objectMapper.writeValueAsString(feed);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(jsonFeed.getBytes());
        }

        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }

    private FeedResponse deserializeAndDecompress(String compressed) throws Exception {
        byte[] compressedBytes = Base64.getDecoder().decode(compressed);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayInputStream bis = new ByteArrayInputStream(compressedBytes);
        try (GZIPInputStream gzipIS = new GZIPInputStream(bis)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIS.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
        }

        String jsonFeed = bos.toString();
        return objectMapper.readValue(jsonFeed, FeedResponse.class);
    }

    public void invalidateCache(Long memberId) {
        redisTemplate.delete(FEED_CACHE_PREFIX + memberId);
    }

    @Transactional
    public void syncToDatabase(Function<Long, FeedResponse> feedBuilder) {
        Set<String> keys = redisTemplate.keys(FEED_CACHE_PREFIX + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            try {
                String memberId = key.substring(FEED_CACHE_PREFIX.length());
                String compressedCache = redisTemplate.opsForValue().get(key);
                if (compressedCache != null) {
                    FeedResponse cachedFeed = deserializeAndDecompress(compressedCache);
                    FeedResponse currentFeed = feedBuilder.apply(Long.parseLong(memberId));

                    if (!cachedFeed.equals(currentFeed)) {
                        cacheFeed(Long.parseLong(memberId), currentFeed);
                    }
                }
            } catch (Exception e) {
                log.error("회원 닉네임 캐시 동기화 중 오류가 발생했습니다. 키: {}", key, e);
                redisTemplate.delete(key);
            }
        }
    }
}