package com.example.demo.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseErrorForm extends ResponseError {
	
	private Map<String, String> errors = new HashMap<String, String>();

	public ResponseErrorForm(int code, String message, Map<String, String> errors) {
		super(code, message);
		this.errors = errors;
	}
	
}
