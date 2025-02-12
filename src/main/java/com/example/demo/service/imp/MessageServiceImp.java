package com.example.demo.service.imp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.ChatRoom;
import com.example.demo.entities.FileMessage;
import com.example.demo.entities.ImageMessage;
import com.example.demo.entities.Message;
import com.example.demo.entities.TextMessage;
import com.example.demo.entities.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.ChatRoomService;
import com.example.demo.service.MessageService;
import com.example.demo.service.S3Service;
import com.example.demo.service.UserService;

@Service
public class MessageServiceImp implements MessageService {

	private UserService userService;
	private MessageRepository messageRepository;
	private ChatRoomService chatRoomService;
	private S3Service s3Service;

	public MessageServiceImp(UserService userService, MessageRepository messageRepository,
			ChatRoomService chatRoomService, S3Service s3Service) {
		this.userService = userService;
		this.messageRepository = messageRepository;
		this.chatRoomService = chatRoomService;
		this.s3Service = s3Service;
	}

	@Override
	public TextMessage createMessageText(String text, String senderId, String chatRoomId) {
		User sender = userService.findById(senderId);
		ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
		
		if(sender == null) throw new ResourceNotFoundException("Không tìm thấy người dùng với id: " + senderId);
		if(chatRoom == null) throw new ResourceNotFoundException("Không tìm thấy nhóm chát với id: " + chatRoomId);
		
		TextMessage message = new TextMessage();
		message.setChatRoom(chatRoom);
		message.setUser(sender);
		message.setContent(text);
		return messageRepository.save(message);
	}

	@Override
	public Message findNewMessageByChatRoomId(String chatRoomId) {
		return messageRepository.findNewMessageByChatRoomId(chatRoomId).orElse(null);
	}

	@Override
	public Page<Message> findMessagesByChatRoomId(String chatRoomId, int page, int size, String sortField,
			String sortDirection) {
		Sort sort = Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
		return messageRepository.findAllByChatRoomId(chatRoomId, PageRequest.of(page, size, sort));
	}

	@Override
	public ImageMessage createMessageImage(MultipartFile image, String senderId, String chatRoomId) throws Exception {
		String imageUrl = "";
		try {
			User sender = userService.findById(senderId);
			ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
			
			if(sender == null) throw new ResourceNotFoundException("Không tìm thấy người dùng với id: " + senderId);
			if(chatRoom == null) throw new ResourceNotFoundException("Không tìm thấy nhóm chát với id: " + chatRoomId);
			
			imageUrl = s3Service.uploadFile(image);
			ImageMessage message = new ImageMessage();
			message.setChatRoom(chatRoom);
			message.setUser(sender);
			message.setImageUrl(imageUrl);
			message.setTypeImage(image.getContentType());
			return messageRepository.save(message);			
		} catch (Exception e) {
			if (imageUrl != "") s3Service.deleteFile(imageUrl);
			throw e;
		}
	}

	@Override
	public FileMessage createFileMessage(MultipartFile file, String senderId, String chatRoomId) throws Exception {
		String fileUrl = "";
		try {
			User sender = userService.findById(senderId);
			ChatRoom chatRoom = chatRoomService.findById(chatRoomId);
			
			if(sender == null) throw new ResourceNotFoundException("Không tìm thấy người dùng với id: " + senderId);
			if(chatRoom == null) throw new ResourceNotFoundException("Không tìm thấy nhóm chát với id: " + chatRoomId);
			
			fileUrl = s3Service.uploadFile(file);
			FileMessage message = new FileMessage();
			message.setChatRoom(chatRoom);
			message.setUser(sender);
			message.setFileUrl(fileUrl);
			message.setTypeFile(file.getContentType());
			return messageRepository.save(message);			
		} catch (Exception e) {
			if (fileUrl != "") s3Service.deleteFile(fileUrl);
			throw e;
		}
	}

}
