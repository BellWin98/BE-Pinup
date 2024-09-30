package com.pinup.global.config;

import com.pinup.global.websocket.WebSocketChatHandler;
import com.pinup.global.websocket.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketChatHandler webSocketChatHandler;
    private final WebSocketInterceptor webSocketInterceptor;

    @Autowired
    public WebSocketConfig(WebSocketChatHandler webSocketChatHandler, WebSocketInterceptor webSocketInterceptor) {
        this.webSocketChatHandler = webSocketChatHandler;
        this.webSocketInterceptor = webSocketInterceptor;
    }

    /**
     * 웹소켓 연결을 위한 설정
     * EndPoint - ws://localhost:8080/chats 에 연결 시 동작할 핸들러는 webSocketChatHandler
     * @param registry
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketChatHandler, "/chats")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins("*");
    }
}
