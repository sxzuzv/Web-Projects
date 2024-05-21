package web.socket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import web.socket.Message;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/hello")   // '/pub/hello' 경로로 메시지 발행에 대한 처리를 담당한다.
    public void message(Message message) {
        // Message 객체에 정의된 channelId에 메시지를 보낸다.
        // '/sub/channel/채널ID'를 구독 중인 클라이언트에게 메시지를 보낸다.
        simpMessageSendingOperations.convertAndSend("/sub/channel" + message.getChannelId(), message);
    }
}