package com.pinup.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pinup.dto.response.FeedResponse;
import com.pinup.global.config.CacheConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;
import java.util.function.Function;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

@Component
@Slf4j
public class FeedCacheManager extends AbstractRedisCacheManager<Long, FeedResponse> {

    @Autowired
    public FeedCacheManager(
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper
    ) {
        super(redisTemplate, objectMapper,
                CacheConfig.builder()
                        .prefix(RedisCacheConstants.FEED_PREFIX)
                        .ttl(RedisCacheConstants.DEFAULT_TTL)
                        .useCompression(true)
                        .build()
        );
    }

    @Override
    protected String serialize(FeedResponse value) throws Exception {
        String jsonValue = objectMapper.writeValueAsString(value);

        if (!cacheConfig.isUseCompression()) {
            return jsonValue;
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(jsonValue.getBytes(StandardCharsets.UTF_8));
            gzipOS.finish();
            return Base64.getEncoder().encodeToString(bos.toByteArray());
        }
    }

    @Override
    protected FeedResponse deserialize(String data) throws Exception {
        String jsonValue;

        if (cacheConfig.isUseCompression()) {
            byte[] compressedBytes = Base64.getDecoder().decode(data);
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ByteArrayInputStream bis = new ByteArrayInputStream(compressedBytes);
                 GZIPInputStream gzipIS = new GZIPInputStream(bis)) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = gzipIS.read(buffer)) != -1) {
                    bos.write(buffer, 0, len);
                }
                jsonValue = bos.toString(StandardCharsets.UTF_8);
            }
        } else {
            jsonValue = data;
        }

        return objectMapper.readValue(jsonValue, FeedResponse.class);
    }

    @Override
    @Transactional
    public void syncCache(Function<Long, FeedResponse> dataProvider) {
        Set<String> keys = redisTemplate.keys(cacheConfig.getPrefix() + "*");
        if (keys == null || keys.isEmpty()) {
            return;
        }

        for (String key : keys) {
            try {
                String idStr = key.substring(cacheConfig.getPrefix().length());
                Long memberId = Long.parseLong(idStr);

                String cachedValue = redisTemplate.opsForValue().get(key);
                if (cachedValue != null) {
                    FeedResponse cachedFeed = deserialize(cachedValue);
                    FeedResponse currentFeed = dataProvider.apply(memberId);

                    if (!cachedFeed.equals(currentFeed)) {
                        putInCache(memberId, currentFeed);
                    }
                }
            } catch (NumberFormatException e) {
                log.error("캐시 키에서 잘못된 회원 ID 형식이 발견되었습니다. 키: {}", key, e);
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.error("피드 캐시 동기화 중 오류가 발생했습니다. 키: {}", key, e);
                redisTemplate.delete(key);
            }
        }
    }
}