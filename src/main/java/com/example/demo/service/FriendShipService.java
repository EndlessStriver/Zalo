package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.FriendShip;

public interface FriendShipService {
	
	FriendShip sendFriendRequest(String senderId, String friendId);
	
	FriendShip acceptFriendRequest(String senderId ,String friendShipId);
	
	void cancelFriendship(String senderId, String friendShipId);
	
	List<FriendShip> getSendedFriendRequest(String senderId);
	
	List<FriendShip> getReceivedFriendRequest(String receiverId);
	
	List<FriendShip> getFriendList(String userId);
	
}
