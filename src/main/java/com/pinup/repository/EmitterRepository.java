package com.pinup.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EmitterRepository {
    private Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter save(String userId, SseEmitter sseEmitter) {
        String key = makeKey(userId);
        emitters.put(key, sseEmitter);
        return sseEmitter;
    }

    public void deleteById(String userId) {
        String key = makeKey(userId);
        emitters.remove(key);
    }

    public Map<String, SseEmitter> findAllEmitterStartsWithByUserId(String userId) {
        String keyPrefix = makeKey(userId);
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(keyPrefix))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String makeKey(String userId) {
        return "SSE:USER:" + userId + ":";
    }
}