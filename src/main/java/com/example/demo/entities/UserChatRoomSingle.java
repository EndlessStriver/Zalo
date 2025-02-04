package com.example.demo.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue("user_chat_room_single")
public class UserChatRoomSingle extends UserChatRoom {

	public UserChatRoomSingle(User user, ChatRoom chatRoom) {
		super(user, chatRoom);
	}
	
}
