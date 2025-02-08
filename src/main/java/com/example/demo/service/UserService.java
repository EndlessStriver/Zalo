package com.example.demo.service;

import java.util.List;

import com.example.demo.entities.User;

public interface UserService {

	List<User> getFriendsAndMessageContacts(String userId, String friendName);

	User findByPhoneNumber(String phoneNumber, String userId);
	
	User findById(String userId);
	
	User updateProfile(User user);
	
	boolean checkPhoneNumberExist(String phoneNumber);
	
	boolean checkEmailExist(String email);

}
