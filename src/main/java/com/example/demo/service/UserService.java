package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.User;

public interface UserService {

	List<User> getFriendsAndMessageContacts (String userId, String friendName);
	
}
