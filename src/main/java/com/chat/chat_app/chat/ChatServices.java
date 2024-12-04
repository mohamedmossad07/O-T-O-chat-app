package com.chat.chat_app.chat;

import com.chat.chat_app.messages.ChatMessage;
import com.chat.chat_app.messages.FromToMessage;
import com.chat.chat_app.messages.UserConnectionMessage;
import com.chat.chat_app.user.User;
import com.chat.chat_app.user.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatServices {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatServices(UserRepository userRepository, ChatRepository chatRepository, SimpMessagingTemplate messagingTemplate) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
        this.messagingTemplate = messagingTemplate;
    }
    public void connectUser(UserConnectionMessage message){
        Optional<User> user = userRepository.findByUsername(message.getUsername());
        if(user.isPresent()){
            user.get().setStatus(true);
            userRepository.save(user.get());
            broadcastAllUsers();
        }
    }

    private void broadcastAllUsers() {
        List<User> users= userRepository.findAll();
        messagingTemplate.convertAndSend("/users/push-all-users",users);
    }

    public void getChatMessages(FromToMessage message) {
        Optional<List<Message>> messages = chatRepository.findBySenderAndReceiverOrReceiverAndSender(message.getFrom(), message.getTo());
        messagingTemplate.convertAndSend("/users/user-messages/"+message.getFrom(),messages.get());
    }

    public void sendMessage(ChatMessage message) {
        User sender = User.builder().username(message.getFrom()).build();
        User receiver = User.builder().username(message.getTo()).build();
        Message message1 = Message.builder().isRead(false).sender(sender).receiver(receiver).content(message.getContent()).sentAt(LocalDateTime.now()).build();
        chatRepository.save(message1);
        messagingTemplate.convertAndSend("/users/"+message.getTo(),message1);
    }

    public void disconnectUser(String username) {
        User user = userRepository.findByUsername(username).get();
        user.setStatus(false);
        userRepository.save(user);
        broadcastAllUsers();
    }
}
