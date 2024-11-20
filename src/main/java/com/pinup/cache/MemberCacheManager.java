package com.pinup.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinup.global.config.CacheConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.function.Function;

import static com.pinup.global.exception.PinUpException.CACHE_OPERATION_ERROR;

@Component
@Slf4j
public class MemberCacheManager extends AbstractRedisCacheManager<String, String> {

    @Autowired
    public MemberCacheManager(
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper
    ) {
        super(redisTemplate, objectMapper,
                CacheConfig.builder()
                        .prefix(RedisCacheConstants.MEMBER_NICKNAME_PREFIX)
                        .ttl(RedisCacheConstants.DEFAULT_TTL)
                        .useCompression(false)
                        .build()
        );
    }

    public void cacheNicknameEmail(String nickname, String email) {
        try {
            redisTemplate.opsForValue().set(
                    RedisCacheConstants.NICKNAME_EMAIL_PREFIX + nickname,
                    email,
                    cacheConfig.getTtl()
            );
        } catch (Exception e) {
            log.error("닉네임-이메일 매핑 캐시 중 오류가 발생했습니다. 닉네임: {}", nickname, e);
            throw CACHE_OPERATION_ERROR;
        }
    }

    public String getCachedEmail(String nickname) {
        try {
            return redisTemplate.opsForValue().get(RedisCacheConstants.NICKNAME_EMAIL_PREFIX + nickname);
        } catch (Exception e) {
            log.error("닉네임에 대한 이메일 조회 중 오류가 발생했습니다. 닉네임: {}", nickname, e);
            throw CACHE_OPERATION_ERROR;
        }
    }

    @Override
    protected String serialize(String value) {
        return value;
    }

    @Override
    protected String deserialize(String data) {
        return data;
    }

    @Override
    @Transactional
    public void syncCache(Function<String, String> dataProvider) {
        Set<String> keys = redisTemplate.keys(getCacheKey("*"));
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            try {
                String email = key.substring(getCacheKey("").length());
                String cachedNickname = redisTemplate.opsForValue().get(key);
                String currentNickname = dataProvider.apply(email);

                if (currentNickname != null && !currentNickname.equals(cachedNickname)) {
                    putInCache(email, currentNickname);
                }
            } catch (Exception e) {
                log.error("회원 닉네임 캐시 동기화 중 오류가 발생했습니다. 키: {}", key, e);
                redisTemplate.delete(key);
            }
        }
    }
}