package com.pinup.service;

import com.pinup.repository.EmitterRepository;
import com.pinup.repository.EventCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmitterRepository emitterRepository;
    private final EventCacheRepository eventCacheRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe(String userId, String lastEventId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        // 503 에러 방지를 위한 더미 이벤트 전송
        sendToClient(emitter, makeEventId(userId), "EventStream Created. [userId=" + userId + "]");

        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = eventCacheRepository.findAllEventCacheStartsWithByUserId(userId);
            events.entrySet().stream()
                    .filter(entry -> entry.getKey().compareTo(lastEventId) > 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    public void sendNotification(String userId, String message) {
        String eventId = makeEventId(userId);

        eventCacheRepository.save(userId, eventId, message);

        emitterRepository.findAllEmitterStartsWithByUserId(userId)
                .forEach((key, emitter) -> {
                    sendToClient(emitter, eventId, message);
                });
    }

    private String makeEventId(String userId) {
        return userId + "_" + System.currentTimeMillis();
    }

    private void sendToClient(SseEmitter emitter, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(eventId);
            throw new RuntimeException("연결 오류!", exception);
        }
    }
}
