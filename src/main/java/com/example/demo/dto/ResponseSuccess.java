package com.example.demo.dto;

import lombok.Data;

@Data
public class ResponseSuccess {
	
	private String status;
	private int code;
	private String message;
	
	public ResponseSuccess(int code, String message) {
		this.status = "success";
		this.code = code;
		this.message = message;
	}
	
}
