package com.example.demo.service;

import org.springframework.data.domain.Page;

import com.example.demo.entities.Message;
import com.example.demo.entities.TextMessage;

public interface MessageService {
	
	TextMessage createMessageText(String text, String senderId, String chatRoomId);
	
	Message findNewMessageByChatRoomId(String chatRoomId);
	
	Page<Message> findMessagesByChatRoomId(String chatRoomId, int page, int size, String sortField, String sortDirection);
	
}
