package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.ChatRoom;

public interface ChatRoomService {
	
	List<ChatRoom> findMyChatRoomsByRoomName (String myId, String roomName);
	
	ChatRoom findById (String chatRoomId);
	
	List<ChatRoom> findMyChatRooms(String myId);
	
}
