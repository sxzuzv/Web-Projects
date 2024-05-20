package web.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration      // 설정 정보 클래스에 부착, 싱글톤 패턴 유지
@EnableWebSocket    // 웹 소켓 서버 사용 정의
public class WebSocketConfiguration implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
                .addHandler(signalingSocketHandler(), "/room")  // 엔드포인트 'url:port/room'
                .setAllowedOrigins("*");    // 클라이언트에서 웹 소켓 서버에 요청 시, 모든 요청을 수용 (CORS)
    }

    @Bean
    public WebSocketHandler signalingSocketHandler() {
        return new web.socket.WebSocketHandler();
    }
}