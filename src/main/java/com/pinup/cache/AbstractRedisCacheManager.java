package com.pinup.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinup.global.config.CacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import java.util.function.Function;

import static com.pinup.global.exception.PinUpException.*;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRedisCacheManager<K, V> implements CacheService<K, V> {
    protected final RedisTemplate<String, String> redisTemplate;
    protected final ObjectMapper objectMapper;
    protected final CacheConfig cacheConfig;

    @Override
    public String getCacheKey(K key) {
        if (key == null) {
            log.error("캐시 키가 null로 입력되었습니다.");
            throw CACHE_KEY_NULL;
        }
        return cacheConfig.getPrefix() + key.toString();
    }

    protected abstract String serialize(V value) throws Exception;
    protected abstract V deserialize(String data) throws Exception;

    @Override
    public V getFromCache(K key, Function<K, V> dataProvider) {
        String cacheKey = getCacheKey(key);
        String cachedValue = redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue != null) {
            try {
                return deserialize(cachedValue);
            } catch (Exception e) {
                log.error("키 [{}]에 대한 캐시 데이터 역직렬화 중 오류가 발생했습니다: {}", key, e.getMessage());
                invalidateCache(key);
                throw CACHE_DESERIALIZATION_ERROR;
            }
        }

        V data = dataProvider.apply(key);
        putInCache(key, data);
        return data;
    }

    @Override
    public void putInCache(K key, V value) {
        try {
            String serialized = serialize(value);
            redisTemplate.opsForValue().set(
                    getCacheKey(key),
                    serialized,
                    cacheConfig.getTtl()
            );
        } catch (Exception e) {
            log.error("키 [{}]에 대한 캐시 데이터 저장 중 오류가 발생했습니다: {}", key, e.getMessage());
            throw CACHE_SERIALIZATION_ERROR;
        }
    }

    @Override
    public void invalidateCache(K key) {
        try {
            redisTemplate.delete(getCacheKey(key));
        } catch (Exception e) {
            log.error("키 [{}]에 대한 캐시 삭제 중 오류가 발생했습니다: {}", key, e.getMessage());
            throw CACHE_OPERATION_ERROR;
        }
    }
}
