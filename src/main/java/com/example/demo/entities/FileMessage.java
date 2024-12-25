package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("file_message")
public class FileMessage extends Message {
	
	@Column(name = "file_url")
	private String fileUrl;
	
	@Column(name = "type_file")
	private String typeFile;
	
}
