package com.pinup.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketStompConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * STOMP에서 사용하는 메시지 브로커 설정
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        /**
         * 해당 주소를 구독하고 있는 모든 클라이언트에게 메시지 전달
         * 메시지를 구독(수신)하는 요청 URL
         * 내장 메시지 브로커를 사용하기 위한 메서드
         * 파라미터로 지정한 prefix가 붙은 메시지를 발행할 경우, 메시지 브로커가 이를 처리함
         */
        registry.enableSimpleBroker("/topic");

        /**
         * 클라이언트에서 보낸 메세지를 받을 prefix
         * 메시지를 발행(송신)하는 요청 URL
         */
        registry.setApplicationDestinationPrefixes("/send");
    }

    /**
     * STOMP 사용 시 웹소켓만 사용할 때와 다르게, 하나의 연결 주소마다 핸들러 클래스를 따로 구현할 필요 없이
     * Controller 방식으로 간편하게 사용 가능
     * cors, SockJS 설정 가능
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        /**
         * 주소: ws://localhost:8080/ws-stomp
         */
        registry.addEndpoint("/ws-stomp") // 연결될 엔드포인트
                .setAllowedOriginPatterns("*")
                .withSockJS(); // 낮은 버전의 브라우저에서도 사용 가능
    }
}
