package web.socket.handler;

import com.jayway.jsonpath.internal.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import web.socket.Message;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WebSocketHandler extends TextWebSocketHandler {    // WebSocketHandler 클래스: 웹 소켓 핸들러로 정의
    // 웹 소켓 세션 정보 Map: 세션 아이디(key) - 세션(value)
    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    // 웹 소켓 연결 시 실행
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

    // 데이터 통신: 메시지 수신 시 실행
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // message.getPayload(): 메시지에 담긴 텍스트 값을 얻어온다.
        // payload: 전송되는 데이터를 의미한다.
        String socketMessage = Utils.concat(message.getPayload());

        for(String key : sessions.keySet()) {
            // wss: 세션 정보 Map에 존재하는 세션 ID들
            WebSocketSession wss = sessions.get(key);
            try {
                // socketMessage를 텍스트 메시지로 변환한 후, 세션 정보 Map에 저장된 세션 ID에 메시지를 보낸다.
                wss.sendMessage(new TextMessage(socketMessage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 웹 소켓 연결 종료 시 실행
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var sessionId = session.getId();

        // 연결이 끊어진 사용자를 세션 정보 Map에서 삭제한다.
        sessions.remove(sessionId);

        final Message message = new Message();
        message.closeConnect();         // 연결 종료
        message.setSender(sessionId);

        sessions.values().forEach(s -> {    // 세션 정보 Map에 저장된 세션을 순회한다.
            try {
                // 모든 세션에 접속 해제를 알린다.
                s.sendMessage(new TextMessage(Utils.toString(message)));
            } catch (Exception e) {
            }
        });
    }

    // 웹 소켓 통신 에러 시 실행
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }
}