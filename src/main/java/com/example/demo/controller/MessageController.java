package com.example.demo.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.PagedResponse;
import com.example.demo.dto.ResponseDataSuccess;
import com.example.demo.entities.Message;
import com.example.demo.service.MessageService;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

	private MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@GetMapping
	public ResponseEntity<?> getMessagesByChatRoomId(@RequestParam(required = true) String chatRoomId,
			@RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "20") int pageSize,
			@RequestParam(defaultValue = "timestamp") String sortField, @RequestParam(defaultValue = "desc") String orderBy) {

		Page<Message> messages = messageService.findMessagesByChatRoomId(chatRoomId, currentPage, pageSize, sortField, orderBy);

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
