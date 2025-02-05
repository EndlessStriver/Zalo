package com.example.demo.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.ChatRoom;
import com.example.demo.repository.ChatRoomRepository;
import com.example.demo.service.ChatRoomService;

@Service
public class ChatRoomServiceImp implements ChatRoomService {
	
	private ChatRoomRepository chatRoomRepository;
	
	public ChatRoomServiceImp(ChatRoomRepository chatRoomRepository) {
		this.chatRoomRepository = chatRoomRepository;
	}

	@Override
	public List<ChatRoom> findMyChatRoomsByRoomName(String myId, String roomName) {
		return chatRoomRepository.findByUserIdAndRoomName(myId, roomName);
	}

	@Override
	public ChatRoom findById(String chatRoomId) {
		return chatRoomRepository.findById(chatRoomId).orElse(null);
	}

	@Override
	public List<ChatRoom> findMyChatRooms(String myId) {
		return chatRoomRepository.findMyChatRooms(myId);
	}

}