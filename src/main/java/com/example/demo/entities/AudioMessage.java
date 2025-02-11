package com.example.demo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("audio_message")
public class AudioMessage extends Message {
	
	@Column(name = "audio_url")
	private String audioUrl;
	
	@Column(name = "type_audio")
	private String typeAudio;
	
}
