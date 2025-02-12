package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.entities.Account;
import com.example.demo.entities.FileMessage;
import com.example.demo.entities.ImageMessage;
import com.example.demo.entities.Message;
import com.example.demo.service.MessageService;
import com.example.demo.unit.MethodUnit;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

	private MessageService messageService;
	private MethodUnit methodUnit;
	private SimpMessagingTemplate messagingTemplate;

	public MessageController(MessageService messageService, MethodUnit methodUnit,
			SimpMessagingTemplate messagingTemplate) {
		this.messageService = messageService;
		this.methodUnit = methodUnit;
		this.messagingTemplate = messagingTemplate;
	}

	@PostMapping("/images")
	public ResponseEntity<?> createMessageImage(@RequestParam(required = true) String chatRoomId,
			@RequestParam(required = true) MultipartFile file, HttpServletRequest httpServletRequest) throws Exception {
		Account account = methodUnit.getAccountFromToken(httpServletRequest);
		ImageMessage imageMessage = messageService.createMessageImage(file, account.getUser().getUserId(), chatRoomId);
		messagingTemplate.convertAndSend("/private/chat/" + chatRoomId, imageMessage);
		ResponseDataSuccess<ImageMessage> responseDataSuccess = new ResponseDataSuccess<ImageMessage>(
				HttpStatus.OK.value(), "Tạo tin nhắn hình ảnh thành công", imageMessage);
		return ResponseEntity.status(HttpStatus.OK).body(responseDataSuccess);
	}

	@PostMapping("/files")
	public ResponseEntity<?> createFileMessage(@RequestParam(required = true) String chatRoomId,
			@RequestParam(required = true) MultipartFile file, HttpServletRequest httpServletRequest) throws Exception {
		Account account = methodUnit.getAccountFromToken(httpServletRequest);
		FileMessage fileMessage = messageService.createFileMessage(file, account.getUser().getUserId(), chatRoomId);
		messagingTemplate.convertAndSend("/private/chat/" + chatRoomId, fileMessage);
		ResponseDataSuccess<FileMessage> responseDataSuccess = new ResponseDataSuccess<FileMessage>(
				HttpStatus.OK.value(), "Tạo tin nhắn file thành công", fileMessage);
		return ResponseEntity.status(HttpStatus.OK).body(responseDataSuccess);
	}

	@GetMapping
	public ResponseEntity<?> getMessagesByChatRoomId(@RequestParam(required = true) String chatRoomId,
			@RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "20") int pageSize,
			@RequestParam(defaultValue = "timestamp") String sortField,
			@RequestParam(defaultValue = "desc") String orderBy) {

		Page<Message> messages = messageService.findMessagesByChatRoomId(chatRoomId, currentPage, pageSize, sortField,
				orderBy);

		PagedResponse<Message> pagedResponse = new PagedResponse<>();
		pagedResponse.setData(messages.getContent());
		pagedResponse.setCurrentPage(messages.getNumber());
		pagedResponse.setTotalItems(messages.getNumberOfElements());
		pagedResponse.setPageSize(messages.getSize());
		pagedResponse.setTotalPages(messages.getTotalPages());

		ResponseDataSuccess<PagedResponse<Message>> responseDataSuccess = new ResponseDataSuccess<PagedResponse<Message>>(
				HttpStatus.OK.value(), "Lấy danh sách tin nhắn nhóm chát thành công", pagedResponse);

		return ResponseEntity.status(HttpStatus.OK).body(responseDataSuccess);
	}

}
