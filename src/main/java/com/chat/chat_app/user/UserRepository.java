package com.chat.chat_app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    public Optional<User> findByUsername(String username);
    public  Optional<User> findByStatus(boolean status);
}
