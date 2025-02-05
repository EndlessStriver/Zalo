package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.entities.Account;
import com.example.demo.entities.ChatRoom;
import com.example.demo.entities.ChatSingle;
import com.example.demo.entities.Message;
import com.example.demo.service.ChatRoomService;
import com.example.demo.service.ChatSingleService;
import com.example.demo.service.MessageService;
import com.example.demo.unit.MethodUnit;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomController {

    private ChatRoomService chatroomService;
    private ChatSingleService chatSingleService;
    private MessageService messageService;
    private MethodUnit methodUnit;

	public ChatRoomController(ChatRoomService chatroomService, ChatSingleService chatSingleService,
			MessageService messageService, MethodUnit methodUnit) {
		this.chatroomService = chatroomService;
		this.chatSingleService = chatSingleService;
		this.messageService = messageService;
		this.methodUnit = methodUnit;
	}
    
    @GetMapping
	public ResponseEntity<?> getChatRooms(HttpServletRequest request) {
		Account account = methodUnit.getAccountFromToken(request);
		List<ChatRoom> chatRooms = chatroomService.findMyChatRooms(account.getUser().getUserId());
		for (ChatRoom chatRoom : chatRooms) {
			Message newMessage = messageService.findNewMessageByChatRoomId(chatRoom.getChatRoomId());
			chatRoom.setNewMessage(newMessage);
		}
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<List<ChatRoom>>(HttpStatus.OK.value(),
				"Lấy danh sách nhóm đang tham gia thành công", chatRooms));
	}

    @GetMapping("/search")
    public ResponseEntity<?> getChatRoomsByRoomNameAndUserId(@RequestParam String roomName, HttpServletRequest request) {
        Account account = methodUnit.getAccountFromToken(request);
        List<ChatRoom> chatRooms = chatroomService.findMyChatRoomsByRoomName(account.getUser().getUserId(), roomName);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDataSuccess<List<ChatRoom>>(HttpStatus.OK.value(),
                "Lấy danh sách nhóm đang tham gia thành công", chatRooms));
    }

    @GetMapping("/between-users")
    public ResponseEntity<?> getChatRoomForUsers(@RequestParam String friendId, HttpServletRequest request) {
        Account account = methodUnit.getAccountFromToken(request);
        ChatSingle chatRoom = chatSingleService.getChatRoomForUsers(account.getUser().getUserId(), friendId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseDataSuccess<ChatSingle>(HttpStatus.OK.value(), "Lấy nhóm chat thành công", chatRoom));
    }

    @PostMapping("/create-single")
    public ResponseEntity<?> createSingleChatRoom(@RequestParam String friendId, HttpServletRequest request) {
        Account account = methodUnit.getAccountFromToken(request);
        ChatSingle chatRoom = chatSingleService.createChatRoomForUsers(account.getUser().getUserId(), friendId);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDataSuccess<ChatSingle>(HttpStatus.CREATED.value(), "Tạo nhóm chat thành công", chatRoom));
    }
}

