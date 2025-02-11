package com.example.demo.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.ImageMessage;
import com.example.demo.entities.Message;
import com.example.demo.entities.TextMessage;

public interface MessageService {
	
	TextMessage createMessageText(String text, String senderId, String chatRoomId);
	
	ImageMessage createMessageImage(MultipartFile image, String senderId, String chatRoomId) throws Exception;
	
	Message findNewMessageByChatRoomId(String chatRoomId);
	
	Page<Message> findMessagesByChatRoomId(String chatRoomId, int page, int size, String sortField, String sortDirection);
	
}
