package com.pinup.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinup.dto.response.MemberResponse;
import com.pinup.dto.response.ProfileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberCacheManager {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String MEMBER_KEY_PREFIX = "member:";
    private static final String PROFILE_KEY_PREFIX = "profile:";
    private static final Duration MEMBER_TTL = Duration.ofHours(1);
    private static final Duration PROFILE_TTL = Duration.ofMinutes(5);

    public Optional<MemberResponse> getMemberCache(Long memberId) {
        String key = MEMBER_KEY_PREFIX + memberId;
        String cachedValue = redisTemplate.opsForValue().get(key);

        if (cachedValue == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(cachedValue, MemberResponse.class));
        } catch (JsonProcessingException e) {
            log.error("멤버 캐시 역직렬화 실패. key: {}", key, e);
            redisTemplate.delete(key);
            return Optional.empty();
        }
    }

    public void putMemberCache(Long memberId, MemberResponse response) {
        String key = MEMBER_KEY_PREFIX + memberId;
        try {
            String value = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, value, MEMBER_TTL);
        } catch (JsonProcessingException e) {
            log.error("멤버 응답 직렬화 실패. key: {}", key, e);
        }
    }

    public Optional<ProfileResponse> getProfileCache(Long memberId) {
        String key = PROFILE_KEY_PREFIX + memberId;
        String cachedValue = redisTemplate.opsForValue().get(key);

        if (cachedValue == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(objectMapper.readValue(cachedValue, ProfileResponse.class));
        } catch (JsonProcessingException e) {
            log.error("프로필 캐시 역직렬화 실패. key: {}", key, e);
            redisTemplate.delete(key);
            return Optional.empty();
        }
    }

    public void putProfileCache(Long memberId, ProfileResponse response) {
        String key = PROFILE_KEY_PREFIX + memberId;
        try {
            String value = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, value, PROFILE_TTL);
        } catch (JsonProcessingException e) {
            log.error("프로필 응답 직렬화 실패. key: {}", key, e);
        }
    }

    public void evictMemberCache(Long memberId) {
        String key = MEMBER_KEY_PREFIX + memberId;
        redisTemplate.delete(key);
    }

    public void evictProfileCache(Long memberId) {
        String key = PROFILE_KEY_PREFIX + memberId;
        redisTemplate.delete(key);
    }

    public void evictAllCaches(Long memberId) {
        evictMemberCache(memberId);
        evictProfileCache(memberId);
    }
}