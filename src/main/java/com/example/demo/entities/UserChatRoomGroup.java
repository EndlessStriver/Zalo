package com.example.demo.entities;


import com.example.demo.entities.enums.RoleChatRoom;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("user_chat_room_group")
public class UserChatRoomGroup extends UserChatRoom {
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private RoleChatRoom role;
	
}
