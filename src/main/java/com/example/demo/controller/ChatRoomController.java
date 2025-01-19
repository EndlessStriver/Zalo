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
import com.example.demo.entities.ChatRoom;
import com.example.demo.service.ChatRoomService;
import com.example.demo.unit.MethodUnit;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomController {

	private ChatRoomService chatroomService;
	private MethodUnit methodUnit;

	public ChatRoomController(ChatRoomService chatroomService, MethodUnit methodUnit) {
		this.chatroomService = chatroomService;
		this.methodUnit = methodUnit;
	}

	@GetMapping
	public ResponseEntity<?> getChatRoomsByRoomNameAndUserId(@RequestParam String roomName,
			HttpServletRequest request) {
		Account account = methodUnit.getAccountFromToken(request);
		List<ChatRoom> chatRooms = chatroomService.getMyChatrooms(account.getUser().getUserId(), roomName);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<List<ChatRoom>>(HttpStatus.OK.value(),
				"Lấy danh sách nhóm đang tham gia thành công", chatRooms));
	}

}
