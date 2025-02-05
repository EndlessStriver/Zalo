package com.example.demo.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("image_message")
public class ImageMessage extends Message {
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "type_image")
	private String typeImage;

	public ImageMessage(String messageId, LocalDateTime timestamp, String imageUrl, String typeImage) {
		super(messageId, timestamp);
		this.imageUrl = imageUrl;
		this.typeImage = typeImage;
	}
	
}
