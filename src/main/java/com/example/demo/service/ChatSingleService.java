package com.example.demo.service;

import com.example.demo.entities.ChatSingle;

public interface ChatSingleService {

	ChatSingle getChatRoomForUsers(String myId, String friendId);

	ChatSingle createChatRoomForUsers(String myId, String friendId);

}
