package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.entities.Account;
import com.example.demo.entities.User;
import com.example.demo.service.UserService;
import com.example.demo.unit.MethodUnit;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	private UserService userService;
	private MethodUnit methodUnit;

	public UserController(UserService userService, MethodUnit methodUnit) {
		this.userService = userService;
		this.methodUnit = methodUnit;
	}

	@GetMapping("/contacts")
	public ResponseEntity<?> getFriendsAndMessageContacts(@RequestParam String friendName, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		List<User> users = userService.getFriendsAndMessageContacts(sender.getUser().getUserId(), friendName);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<List<User>>(HttpStatus.OK.value(),
				"Lấy danh sách người dùng đã tương tác thành công", users));
	}
	
	@GetMapping("/search-by-phone")
	public ResponseEntity<?> getUserByPhoneNumber(@RequestParam String phoneNumber, HttpServletRequest request) {
		Account sender = methodUnit.getAccountFromToken(request);
		User user = userService.findByPhoneNumber(phoneNumber, sender.getUser().getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseDataSuccess<User>(HttpStatus.OK.value(), "Tìm bạn bè thành công", user));
	}
}

