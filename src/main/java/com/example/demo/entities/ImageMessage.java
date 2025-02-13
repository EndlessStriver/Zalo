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
	
	@Column(name = "image_url", nullable = true)
	private String imageUrl;
	
	@Column(name = "image_name", nullable = true)
	private String imageName;
	
	@Column(name = "type_image", nullable = true)
	private String typeImage;

}
