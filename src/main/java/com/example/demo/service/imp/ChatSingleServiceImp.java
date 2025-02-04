package com.example.demo.service.imp;

import org.springframework.stereotype.Service;

import com.example.demo.entities.ChatSingle;
import com.example.demo.entities.User;
import com.example.demo.entities.UserChatRoomSingle;
import com.example.demo.repository.ChatSingleRepository;
import com.example.demo.service.ChatSingleService;
import com.example.demo.service.UserService;

@Service
public class ChatSingleServiceImp implements ChatSingleService {
	
	private ChatSingleRepository chatSingleRepository;
	private UserService userService;
	
	public ChatSingleServiceImp(ChatSingleRepository chatSingleRepository, UserService userService) {
		this.chatSingleRepository = chatSingleRepository;
		this.userService = userService;
	}

	@Override
	public ChatSingle getChatRoomForUsers(String myId, String friendId) {
		return chatSingleRepository.findChatRoomForUsers(myId, friendId).orElse(null);
	}

	@Override
	public ChatSingle createChatRoomForUsers(String myId, String friendId) {
		ChatSingle chatSingle = new ChatSingle();
		
		User myUser = userService.findById(friendId);
		User friendUser = userService.findById(friendId);
		
		UserChatRoomSingle userChatRoomSingle1 = new UserChatRoomSingle(myUser, chatSingle);
		UserChatRoomSingle userChatRoomSingle2 = new UserChatRoomSingle(friendUser, chatSingle);
		
		chatSingle.getUserChatRooms().add(userChatRoomSingle1);
		chatSingle.getUserChatRooms().add(userChatRoomSingle2);
		
		return chatSingleRepository.save(chatSingle);
	}

}
