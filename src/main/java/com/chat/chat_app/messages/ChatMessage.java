package com.chat.chat_app.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    private String username;
    private String from;
    private String to;
    private String content;
    private String sentAt;
}
