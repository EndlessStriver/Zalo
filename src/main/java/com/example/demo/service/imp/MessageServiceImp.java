package com.example.demo.service.imp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.entities.ChatRoom;
import com.example.demo.entities.Message;
import com.example.demo.entities.TextMessage;
import com.example.demo.entities.User;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.ChatRoomService;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;

@Service
public class MessageServiceImp implements MessageService {

	private UserService userService;
	private MessageRepository messageRepository;
	private ChatRoomService chatRoomService;

	public MessageServiceImp(UserService userService, MessageRepository messageRepository,
			ChatRoomService chatRoomService) {
		this.userService = userService;
		this.messageRepository = messageRepository;
		this.chatRoomService = chatRoomService;
	}

	@Override
	public TextMessage createMessageText(String text, String senderId, String chatRoomId) {
		User sender = userService.findById(senderId);
		ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
		TextMessage message = new TextMessage();
		message.setChatRoom(chatRoom);
		message.setUser(sender);
		message.setContent(text);
		return messageRepository.save(message);
	}

	@Override
	public Message findNewMessageByChatRoomId(String chatRoomId) {
		return messageRepository.findNewMessageByChatRoomId(chatRoomId).orElse(null);
	}

	@Override
	public Page<Message> findMessagesByChatRoomId(String chatRoomId, int page, int size, String sortField,
			String sortDirection) {
		Sort sort = Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
		return messageRepository.findAllByChatRoomId(chatRoomId, PageRequest.of(page, size, sort));
	}

}
