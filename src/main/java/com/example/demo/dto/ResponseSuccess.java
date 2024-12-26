package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSuccess<T> {
	private String status;
	private int code;
	private String message;
	private T data;
	
	public ResponseSuccess(int code, String message, T data) {
		this.status = "success";
		this.code = code;
		this.message = message;
		this.data = data;
	}
}
