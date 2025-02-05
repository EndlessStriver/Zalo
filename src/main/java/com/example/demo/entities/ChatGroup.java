package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("chat_group")
public class ChatGroup extends ChatRoom{
	
	@Column(name = "room_name")
	private String roomName;
	
	@Column(name = "room_image")
	private String roomImage;

}
