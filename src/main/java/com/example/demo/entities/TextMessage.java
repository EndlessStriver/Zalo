package com.example.demo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@DiscriminatorValue("text_message")
public class TextMessage extends Message {
	
	@Column(name = "content")
	private String content;

	public TextMessage(String messageId, LocalDateTime timestamp, String content) {
		super(messageId, timestamp);
		this.content = content;
	}
	
}
