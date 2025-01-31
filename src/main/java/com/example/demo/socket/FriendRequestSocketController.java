package com.example.demo.socket;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class FriendRequestSocketController {
	
	private final SimpMessagingTemplate messagingTemplate;

	public FriendRequestSocketController(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}
	
	@MessageMapping("/friend-request/send")
    public void sendFriendRequest(@Header("receiveId") String receiveId) {
        messagingTemplate.convertAndSend("/private/friend-request-receive/" + receiveId, "Bạn có lời mời kết bạn mới!");
    }
	
	@MessageMapping("/friend-request/cancel")
    public void cancelFriendRequest(@Header("receiveId") String receiveId) {
        messagingTemplate.convertAndSend("/private/friend-request-cancel/" + receiveId, receiveId);
    }

}
