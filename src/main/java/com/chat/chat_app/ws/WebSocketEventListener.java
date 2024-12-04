package com.chat.chat_app.ws;

import com.chat.chat_app.chat.ChatServices;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    private final ChatServices chatServices;

    public WebSocketEventListener(ChatServices chatServices) {
        this.chatServices = chatServices;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            chatServices.disconnectUser(username);
            // Handle disconnection logic, like broadcasting to other users
        }
    }
}