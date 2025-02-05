package com.example.demo.socket;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.entities.Message;
import com.example.demo.service.MessageService;

@Controller
public class ChatSocketController {

	private final SimpMessagingTemplate messagingTemplate;
	private MessageService messageService;

	public ChatSocketController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
		this.messagingTemplate = messagingTemplate;
		this.messageService = messageService;
	}

	@MessageMapping("/chat/send")
	public void sendChat(@Header("senderId") String senderId, @Header("chatRoomId") String chatRoomId,
			@Payload String message) {
		Message myMessage = messageService.createMessageText(message, senderId, chatRoomId);
		messagingTemplate.convertAndSend("/private/chat/" + chatRoomId, myMessage);
	}

}
