package com.example.demo.service;

import com.example.demo.entities.Message;
import com.example.demo.entities.TextMessage;

public interface MessageService {
	
	TextMessage createMessageText(String text, String senderId, String chatRoomId);
	
	Message findNewMessageByChatRoomId(String chatRoomId);
	
}
