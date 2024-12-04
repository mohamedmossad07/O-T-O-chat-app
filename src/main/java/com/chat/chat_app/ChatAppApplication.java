package com.chat.chat_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class ChatAppApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		SpringApplication.run(ChatAppApplication.class, args);

	}



}
