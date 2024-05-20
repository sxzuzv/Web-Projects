package web.socket;

import com.jayway.jsonpath.internal.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {    // WebSocketHandler 클래스: 웹 소켓 핸들러로 정의
    // 웹 소켓 세션 정보 Map: 세션 아이디(key) - 세션(value)
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 웹 소켓 연결
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        var sessionId = session.getId();
        sessions.put(sessionId, session);   // 접속한 세션을 세션 정보 Map에 저장한다.

        Message message = Message.builder().sender(sessionId).receiver("all").build();
        message.newConnect();   // 연결 상태 UPDATE: "NEW"
        
        sessions.values().forEach(s -> {    // 세션 정보 Map에 저장된 세션을 순회한다.
            try {
                if(!s.getId().equals(sessionId)) {  // 본인이 아닌 연결된 모든 세션에 접속을 알린다.
                    s.sendMessage(new TextMessage(Utils.toString(message)));
                }
            }
            catch (Exception e) {
                
            }
        });
    }

    // 데이터 통신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

    }

    // 웹 소켓 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

    }

    // 웹 소켓 통신 에러
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }
}