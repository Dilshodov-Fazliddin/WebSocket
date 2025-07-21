package com.app.websocket.config;

import com.app.websocket.chat.ChatMessage;
import com.app.websocket.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener   {

    private final SimpMessageSendingOperations simpMessageSendingOperations;
    @EventListener
    public void handleWebSocketDisconnectListener
            (SessionDisconnectEvent sessionDisconnectEvent)
    {
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("username: {} disconnected", username);
           var chat= ChatMessage.builder()
                    .type(MessageType.LEAVER)
                    .sender(username)
            .build();
            simpMessageSendingOperations.convertAndSend( "/topic/leaver",  chat );
        }
    }
}
