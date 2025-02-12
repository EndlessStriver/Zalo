package com.example.demo.socket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.example.demo.service.MessageService;

@Controller
public class ChatSocketController {

	private SimpMessagingTemplate messagingTemplate;
	private MessageService messageService;

	public ChatSocketController(SimpMessagingTemplate messagingTemplate, MessageService messageService) {
		this.messagingTemplate = messagingTemplate;
		this.messageService = messageService;
	}

}
