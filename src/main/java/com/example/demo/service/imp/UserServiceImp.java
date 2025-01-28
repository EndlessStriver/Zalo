package com.example.demo.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entities.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

@Service
public class UserServiceImp implements UserService {
	
	private UserRepository userRepository;
	
	public UserServiceImp(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<User> getFriendsAndMessageContacts(String userId, String friendName) {
		return userRepository.findFriendsByFullnameAndUserId(userId, friendName);
	}

	@Override
	public User findByPhoneNumber(String phoneNumber, String userId) {
		return userRepository.findByPhoneNumber(phoneNumber);
	}

}
