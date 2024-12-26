package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {
	private String status;
	private int code;
	private String message;
	private List<Map<String, String>> errors = new ArrayList<Map<String,String>>();
	
	public ResponseError(int code, String message) {
		this.status = "error";
		this.code = code;
		this.message = message;
	}
	
	public ResponseError(int code, String message, List<Map<String, String>> errors) {
		this.status = "error";
		this.code = code;
		this.message = message;
		this.errors = errors;
	}
}
