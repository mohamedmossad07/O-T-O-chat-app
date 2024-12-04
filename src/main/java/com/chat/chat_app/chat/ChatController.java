package com.chat.chat_app.chat;

import com.chat.chat_app.messages.ChatMessage;
import com.chat.chat_app.messages.FromToMessage;
import com.chat.chat_app.messages.UserConnectionMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Controller
public class ChatController {
    private final ChatServices chatServices;

    public ChatController(ChatServices chatServices) {
        this.chatServices = chatServices;
    }

    @GetMapping("/chat")
    public String chat(Model model, HttpServletRequest request){
        Optional<Cookie> username = Arrays.stream(request.getCookies()).filter(cookie -> Objects.equals(cookie.getName(), "username")).findFirst();
        if (username.isEmpty() || username.get().getValue().trim().isEmpty())
            return "redirect:/login";
        return "chat";
    }
    @MessageMapping("/connect-user")
    public void connect(@Payload UserConnectionMessage message){
        chatServices.connectUser(message);
    }
    @MessageMapping("/user-messages")
    public void userMessages(@Payload FromToMessage message){
        chatServices.getChatMessages(message);
    }
    @MessageMapping("/send-message")
    public void sendMessage(@Payload ChatMessage message){
        chatServices.sendMessage(message);
    }
}
