package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDataSuccess<T> extends ResponseSuccess {
	
	private T data;

	public ResponseDataSuccess(int code, String message, T data) {
		super(code, message);
		this.data = data;
	}
	
}
