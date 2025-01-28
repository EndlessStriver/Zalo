package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.FriendShip;
import com.example.demo.entities.enums.FriendType;

public interface FriendShipService {
	
	FriendShip sendFriendRequest(String senderId, String friendId);
	
	FriendShip acceptFriendRequest(String senderId ,String friendId);
	
	void cancelFriendship(String senderId, String friendId);
	
	List<FriendShip> getSendedFriendRequest(String senderId);
	
	List<FriendShip> getReceivedFriendRequest(String receiverId);
	
	List<FriendShip> getFriendList(String userId);
	
	FriendType checkFriendshipByPhoneNumber(String phoneNumber, String senderId);
}
