package com.chat.chat_app.ws;

import com.chat.chat_app.chat.ChatHelper;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;
import java.util.Objects;

public class MyHandshakeInterceptor  extends HttpSessionHandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String username = ChatHelper.extractValueFromStringCookie("username", Objects.requireNonNull(request.getHeaders().get("cookie")).getFirst());
        if(username!=null)
            attributes.put("username",username);
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }
}
