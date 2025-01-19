package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.ChatRoom;

public interface ChatRoomService {
	
	List<ChatRoom> getMyChatrooms (String userId, String roomName);
	
}
