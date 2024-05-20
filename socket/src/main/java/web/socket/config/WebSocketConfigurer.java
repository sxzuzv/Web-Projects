package web.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker   // STOMP 사용
public class WebSocketConfigurer implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // SUB(구독) 경로: "/sub/channel/채널 ID" (Prefix: "/sub")
        // PUB(발송): "/pub/hello", 메시지에 채널 ID를 포함한다. (Prefix: "/pub")
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")   // 엔드포인트 'url:port/ws'
                .setAllowedOrigins("*");    // 클라이언트에서 웹 소켓 서버에 요청 시, 모든 요청을 수용 (CORS)
    }
}