package com.chat.chat_app.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Message,Long> {
    @Query("SELECT m FROM Message m WHERE (m.sender.username = :sender AND m.receiver.username = :receiver) OR (m.sender.username = :receiver AND m.receiver.username = :sender) order by m.sentAt asc")
    public Optional<List<Message>> findBySenderAndReceiverOrReceiverAndSender(@Param("sender") String sender,@Param("receiver") String receiver);
}
