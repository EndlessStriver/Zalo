package com.example.demo.dto;

import lombok.Data;

@Data
public class ResponseError {
	
	private String status;
	private int code;
	private String message;
	
	public ResponseError(int code, String message) {
		this.status = "error";
		this.code = code;
		this.message = message;
	}
	
}
