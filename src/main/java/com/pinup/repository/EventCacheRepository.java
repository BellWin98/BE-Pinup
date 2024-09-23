package com.pinup.repository;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class EventCacheRepository {
    private Map<String, String> eventCache = new ConcurrentHashMap<>();

    public void save(String userId, String eventId, String message) {
        eventCache.put(makeKey(userId, eventId), message);
    }

    public Map<String, Object> findAllEventCacheStartsWithByUserId(String userId) {
        String keyPrefix = makeKey(userId, "");
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(keyPrefix))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().substring(keyPrefix.length()),
                        Map.Entry::getValue
                ));
    }

    private String makeKey(String userId, String eventId) {
        return "EVENT:USER:" + userId + ":" + eventId;
    }
}