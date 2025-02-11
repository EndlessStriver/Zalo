package com.example.demo.entities;

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
	
	@Column(name = "image_url", nullable = false)
	private String imageUrl;
	
	@Column(name = "type_image", nullable = false)
	private String typeImage;

}
