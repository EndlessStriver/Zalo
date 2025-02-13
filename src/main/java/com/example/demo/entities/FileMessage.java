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
	
	@Column(name = "file_url", nullable = true)
	private String fileUrl;
	
	@Column(name = "file_name", nullable = true)
	private String fileName;
	
	@Column(name = "type_file", nullable = true)
	private String typeFile;
	
}
